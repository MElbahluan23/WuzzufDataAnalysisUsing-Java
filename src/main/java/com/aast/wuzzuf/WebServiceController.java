/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aast.wuzzuf;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebServiceController {
    
    DAOImplemnt dao;

    public WebServiceController() throws Exception {
        this.dao = new DAOImplemnt();
    }
    
    @RequestMapping("/somejobs")
    public List<Job> getSomeJobs(){
        return dao.toJoblist(dao.displaySomeData());
    }
    
    @RequestMapping("/schema")
    public List<Schema> getSchema(){
        return dao.toSchemalist(dao.displaySchema());
    }
    
    @RequestMapping("/summary")
    public List<Summary> getSummary(){
        return dao.toSummarylist(dao.displaySummary());
    }

    @RequestMapping("/datacount")
    public Map<String, Integer> getDataSummary(){
        return dao.dataSummary();
    }
    
    @RequestMapping("/cleandata")
    public List<Job> cleanData(){
        return dao.toFactorizedJoblist(dao.cleanData().slice(0, 100));
    }
    
    @RequestMapping("/mostdemandingcompanies")
    public Map MostDemandingCompanies(){
        return dao.countJobsForCompany();
    }
    
    @RequestMapping("/mostpopularjob")
    public Map mostPopularJobTitle(){
        return dao.popularJobTitles();
    }
    
    @RequestMapping("/mostpopularareas")
    public Map mostPopularAreas(){
        return dao.popularAreas();
    }
    
    @RequestMapping("/importantskills")
    public Map mostImportantSkill(){
        return dao.importantSkills();
    }

    @RequestMapping("/kmeans")
    @GetMapping
    public List<List<ClusterObject>> kmeans(@RequestParam(value = "num") int num){
        return dao.kMeans(num);
    }
}
