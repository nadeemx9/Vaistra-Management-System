package com.vaistra.services;

import com.opencsv.CSVWriter;
import com.vaistra.config.spring_batch.CountryBatch.CountryWriter;
import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.MessageResponse;
import com.vaistra.entities.DemoCSV;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.repositories.DemoRepository;
import com.vaistra.utils.AppUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.support.PassThroughItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class DemoService {
    private final DemoRepository demoRepository;
    private final JobLauncher jobLauncher;
    private final Job job;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final AppUtils appUtils;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public DemoService(DemoRepository demoRepository, JobLauncher jobLauncher, @Qualifier("demoReaderJob")  Job job, JobRepository jobRepository, PlatformTransactionManager transactionManager, AppUtils appUtils) {
        this.demoRepository = demoRepository;
        this.jobLauncher = jobLauncher;
        this.job = job;
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

    public byte[] exportCsvToZip() throws IOException, JobExecutionException {
        return null;
    }


//    public HttpResponse showData(int pageNumber, int pageSize, String sortDirection,String date1,String date2){
//        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
//                Sort.by("date").ascending().and(Sort.by("time").ascending())
//                : Sort.by("date").descending().and(Sort.by("time").descending());
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//
//        LocalDate date_1 = LocalDate.parse(date1,formatter);
//
//        LocalDate date_2 = LocalDate.parse(date2,formatter);
//
//        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
//
//        Page<DemoCSV> response = demoRepository.findByDateBetween(date_1,date_2,pageable);
//
//        List<DemoCSV> result = response.stream().toList();
//
//        return HttpResponse.builder()
//                .pageNumber(response.getNumber())
//                .pageSize(response.getSize())
//                .totalElements(response.getTotalElements())
//                .totalPages(response.getTotalPages())
//                .isLastPage(response.isLast())
//                .data(result)
//                .build();
//    }

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

//    public HttpResponse showData(int pageNumber, int pageSize, String sortDirection, String date1, String date2){
//        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
//                Sort.by("date").ascending().and(Sort.by("time").ascending())
//                : Sort.by("date").descending().and(Sort.by("time").descending());
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//        LocalDate startDate = LocalDate.parse(date1, formatter);
//        LocalDate endDate = LocalDate.parse(date2, formatter);
//
//        PageRequest pageRequest = PageRequest.of(pageNumber,pageSize,sort);
//
//        // Your custom JPQL query
//        String jpql = "SELECT d FROM DemoCSV d " +
//                "WHERE d.date BETWEEN :startDate AND :endDate "  +
//                "ORDER BY d.date,d.time";
//
//        TypedQuery<DemoCSV> query = entityManager.createQuery(jpql, DemoCSV.class);
//        query.setParameter("startDate", startDate);
//        query.setParameter("endDate", endDate);
//        query.setFirstResult(pageRequest.getPageNumber() * pageRequest.getPageSize());
//        query.setMaxResults(pageRequest.getPageSize());
//
//        List<DemoCSV> yourResultsList = query.getResultList();
//
//        String countJpql = "SELECT COUNT(d.id) FROM DemoCSV d " +
//                "WHERE d.date BETWEEN :startDate AND :endDate";
//        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);
//        countQuery.setParameter("startDate", startDate);
//        countQuery.setParameter("endDate", endDate);
//        long totalCount = countQuery.getSingleResult();
//
//        Page<DemoCSV> result = new PageImpl<>(yourResultsList, pageRequest, totalCount);
//        List<DemoCSV> respose  = result.stream().toList();
//
//        return HttpResponse.builder()
//                .pageNumber(result.getNumber())
//                .pageSize(result.getSize())
//                .totalElements(result.getTotalElements())
//                .totalPages(result.getTotalPages())
//                .isLastPage(result.isLast())
//                .data(respose)
//                .build();
//    }
//    public HttpResponse showData(int pageNumber, int pageSize, String sortDirection, String date1, String date2){
//        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
//                Sort.by("date").ascending().and(Sort.by("time").ascending())
//                : Sort.by("date").descending().and(Sort.by("time").descending());
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//        LocalDate startDate = LocalDate.parse(date1, formatter);
//        LocalDate endDate = LocalDate.parse(date2, formatter);
//
//        PageRequest pageRequest = PageRequest.of(pageNumber,pageSize,sort);
//
//        // Your custom JPQL query
//        String jpql = "SELECT d FROM DemoCSV d " +
//                "WHERE d.date BETWEEN :startDate AND :endDate " +
//                "ORDER BY d.date,d.time";
//
//        TypedQuery<DemoCSV> query = entityManager.createQuery(jpql, DemoCSV.class);
//        query.setParameter("startDate", startDate);
//        query.setParameter("endDate", endDate);
//        query.setFirstResult(pageRequest.getPageNumber() * pageRequest.getPageSize());
//        query.setMaxResults(pageRequest.getPageSize());
//
//        List<DemoCSV> yourResultsList = query.getResultList();
//
//        String countJpql = "SELECT COUNT(d.id) FROM DemoCSV d " +
//                "WHERE d.date BETWEEN :startDate AND :endDate";
//
//        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);
//        countQuery.setParameter("startDate", startDate);
//        countQuery.setParameter("endDate", endDate);
//        long totalCount = countQuery.getSingleResult();
//
//        Page<DemoCSV> result = new PageImpl<>(yourResultsList, pageRequest, totalCount);
//
//        return HttpResponse.builder()
//                .pageNumber(result.getNumber())
//                .pageSize(result.getSize())
//                .totalElements(result.getTotalElements())
//                .totalPages(result.getTotalPages())
//                .isLastPage(result.isLast())
//                .data(result.stream().toList())
//                .build();
//    }

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

//    public MessageResponse exportToExcel(HttpServletResponse response) throws IOException {
//
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        String exportFilePath = "D:\\Downloaded_Excel\\users.xlsx";
////        response.setHeader("Content-Disposition", "attachment; filename=\"User.xlsx\"");
//
//        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
//        String currentDateTime = dateFormatter.format(new Date());
//
//        String headerKey = "Content-Disposition";
//        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
//        response.setHeader(headerKey, headerValue);
//
//        LocalDate startDate = LocalDate.of(2010,1,1);
//        LocalDate endDate = LocalDate.of(2010,6,30);
//
//        List<DemoCSV> listUsers = demoRepository.findByDateBetween(startDate,endDate);
//
//        UserExcelExporter excelExporter = new UserExcelExporter(listUsers);
//
//
//        try {
//            excelExporter.export(exportFilePath);
//            return new MessageResponse(true, "Excel file exported to " + exportFilePath);
//        } catch (IOException e) {
//            return new MessageResponse(false, "Failed to export Excel file.");
//        }
//    }
//
//
//    public void exportToCSV(HttpServletResponse response) throws IOException {
//
//        String downloadPath = "D:\\Downloaded_Excel\\Users.csv";
//
//        response.setContentType("text/csv");
//        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
//        String currentDateTime = dateFormatter.format(new Date());
//
//        String headerKey = "Content-Disposition";
//        String headerValue = "attachment; filename=" + downloadPath + "users_" + currentDateTime + ".csv";
//
////        String headerValue = "attachment; filename=users_" + currentDateTime + ".csv";
//        response.setHeader(headerKey, headerValue);
//
//        List<DemoCSV> listUsers = demoRepository.findAll();
//        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
//        String[] csvHeader = {"User ID", "Employee-Name", "E-mail", "Roles"};
//        String[] nameMapping = {"userId", "fullName", "email", "role"};
//
//        csvWriter.writeHeader(csvHeader);
//
//        for (DemoCSV user : listUsers) {
//            csvWriter.write(user, nameMapping);
//
//        }
//
//
//        csvWriter.close();
//
//
//    }
//
//    public MessageResponse exportToPDF(HttpServletResponse response) throws IOException {
//
//
//        String exportFilePath = "D:\\Downloaded_Excel\\users.pdf";
//        response.setContentType("application/pdf");
//        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD : HH:MM:SS");
//        String currentDateTime = dateFormat.format(new Date());
//        String headerkey = "Content-Disposition";
//        String headervalue = "attachment; filename=" + exportFilePath + "User_" + currentDateTime + ".pdf";
//        response.setHeader(headerkey, headervalue);
//        List<DemoCSV> listofUsers = demoRepository.findAll();
//        PdfGenerator generator = new PdfGenerator(exportFilePath, currentDateTime);
//        generator.generate(listofUsers, response);
//        return null;
//    }
}