package com.vaistra.services;

import com.vaistra.config.spring_batch.CountryBatch.CountryWriter;
import com.vaistra.dto.HttpResponse;
import com.vaistra.entities.DemoCSV;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.repositories.DemoRepository;
import com.vaistra.utils.AppUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class DemoService {
    private final DemoRepository demoRepository;
    private final JobLauncher jobLauncher;
    private final Job job;
    private final Job job2;
    private final AppUtils appUtils;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public DemoService(DemoRepository demoRepository, JobLauncher jobLauncher, @Qualifier("demoReaderJob")  Job job,@Qualifier("demoGetDataJob") Job job2, AppUtils appUtils) {
        this.demoRepository = demoRepository;
        this.jobLauncher = jobLauncher;
        this.job = job;
        this.job2 = job2;
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

    public List<DemoCSV> temp(){
        return null;
    }
}