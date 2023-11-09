package com.vaistra.services;

import com.opencsv.CSVWriter;
import com.vaistra.config.spring_batch.CountryBatch.CountryWriter;
import com.vaistra.dto.HttpResponse;
import com.vaistra.entities.DemoCSV;
import com.vaistra.entities.cscv.Country;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.repositories.DemoRepository;
import com.vaistra.repositories.cscv.CountryRepository;
import com.vaistra.utils.AppUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class DemoService {
    private final DemoRepository demoRepository;
    private final JobLauncher jobLauncher;
    private final Job job;
    private final CountryRepository countryRepository;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final AppUtils appUtils;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public DemoService(DemoRepository demoRepository, JobLauncher jobLauncher, @Qualifier("demoReaderJob")  Job job, CountryRepository countryRepository, JobRepository jobRepository, PlatformTransactionManager transactionManager, AppUtils appUtils) {
        this.demoRepository = demoRepository;
        this.jobLauncher = jobLauncher;
        this.job = job;
        this.countryRepository = countryRepository;
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.appUtils = appUtils;
    }

    public String importData(MultipartFile file) {
        if(file == null)
            throw new ResourceNotFoundException("CSV File is not Uploaded   ");
        if(file.isEmpty())
            throw new ResourceNotFoundException("Country CSV File not found...!");
        if(!Objects.equals(file.getContentType(), "text/csv"))
            throw new IllegalArgumentException("Invalid file type. Please upload a CSV file.");
        if(!appUtils.isSupportedExtensionBatch(file.getOriginalFilename()))
            throw new ResourceNotFoundException("Only CSV and Excel File is Accepted");

        try {
            File tempFile = File.createTempFile(LocalDate.now().format(dateFormatter) + "_" + LocalTime.now().format(timeFormatter) + "_Country_" +"temp", ".csv");
            String orignalFileName = file.getOriginalFilename();
            assert orignalFileName != null;
            file.transferTo(tempFile);


                System.out.println(tempFile.getAbsolutePath());

                JobParameters jobParameters = new JobParametersBuilder()
                        .addString("inputFileDemo", tempFile.getAbsolutePath())
                        .toJobParameters();

                JobExecution execution =  jobLauncher.run(job, jobParameters);

                if (execution.getExitStatus().equals(ExitStatus.COMPLETED)){
                    System.out.println("Job is Completed");
                    if(tempFile.exists()){
                        if(tempFile.delete())
                            System.out.println("File Deleted");
                        else
                            System.out.println("Can't Delete File");
                    }
                    CountryWriter.setCounter(0);
                }

            return "CSV file uploaded successfully.";

        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }


    public HttpResponse showData(int pageNumber, int pageSize, String sortDirection,String date1,String date2){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate startDate = LocalDate.parse(date1, formatter);
        LocalDate endDate = LocalDate.parse(date2, formatter);

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<DemoCSV> criteriaQuery = criteriaBuilder.createQuery(DemoCSV.class);
        Root<DemoCSV> root = criteriaQuery.from(DemoCSV.class);

        criteriaQuery.select(root)
                .where(criteriaBuilder.between(root.get("date"), startDate, endDate));

        // Define the sorting based on the "sortDirection" parameter
        Path<LocalDate> datePath = root.get("date");
        Path<LocalTime> timePath = root.get("time");
        Order dateOrder = sortDirection.equalsIgnoreCase("asc") ? criteriaBuilder.asc(datePath) : criteriaBuilder.desc(datePath);
        Order timeOrder = sortDirection.equalsIgnoreCase("asc") ? criteriaBuilder.asc(timePath) : criteriaBuilder.desc(timePath);
        criteriaQuery.orderBy(dateOrder, timeOrder);


        TypedQuery<DemoCSV> query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult((int) pageRequest.getOffset());
        query.setMaxResults(pageRequest.getPageSize());
        List<DemoCSV> yourResultsList = query.getResultList();

        CriteriaQuery<Long> countCriteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<DemoCSV> countRoot = countCriteriaQuery.from(DemoCSV.class);

        Predicate datePredicate = criteriaBuilder.between(countRoot.get("date"), startDate, endDate);
        countCriteriaQuery.select(criteriaBuilder.count(countRoot))
                .where(datePredicate);

        TypedQuery<Long> countQuery = entityManager.createQuery(countCriteriaQuery);

        long totalCount = countQuery.getSingleResult();

        Page<DemoCSV> result = new PageImpl<>(yourResultsList, pageRequest, totalCount);

        return HttpResponse.builder()
                .pageNumber(result.getNumber())
                .pageSize(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .isLastPage(result.isLast())
                .data(result.stream().toList())
                .build();
    }

    public void getChunkedData(ServletOutputStream outputStream, String date1, String date2) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate startDate = LocalDate.parse(date1, formatter);
        LocalDate endDate = LocalDate.parse(date2, formatter);

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DemoCSV> criteriaQuery = criteriaBuilder.createQuery(DemoCSV.class);
        Root<DemoCSV> root = criteriaQuery.from(DemoCSV.class);

        criteriaQuery.select(root)
                .where(criteriaBuilder.between(root.get("date"), startDate, endDate));

        TypedQuery<DemoCSV> query = entityManager.createQuery(criteriaQuery);

        // Use the query result to create a CSV format and write it to the output stream
        try (CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {
            // Write the header row if needed
            String[] header = {"Column1", "Column2", "Column3"}; // Replace with your column names
            csvWriter.writeNext(header);

            query.getResultList().forEach(demoCSV -> {
                String[] row = {
                        demoCSV.getId().toString(),
                        demoCSV.getDate().toString(),
                        demoCSV.getTime().toString()
                };
                csvWriter.writeNext(row);
            });
        }
    }

//    public void exportExcelData(String absolutePath, HttpServletResponse response, String date1, String date2) {
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//
//        LocalDate startDate = LocalDate.parse(date1, formatter);
//        LocalDate endDate = LocalDate.parse(date2, formatter);
//
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
////        response.setContentType("application/octet-stream");
////        response.setHeader("Content-Disposition", "attachment; filename=\"User.xlsx\"");
//
//        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
//
//        String headerKey = "Content-Disposition";
//        String headerValue = "attachment; filename=ExportExcel"+ "demoEXCELExport.xlsx";
//        response.setHeader(headerKey, headerValue);
//
//        List<Country> csvList = countryRepository.findAll();
//
//        try {
//            excelGenerator.export(response,absolutePath);
//        } catch (IOException e) {
//            System.out.println(e);
//        }
//    }
}