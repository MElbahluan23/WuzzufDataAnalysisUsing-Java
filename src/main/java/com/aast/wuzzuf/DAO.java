
package com.aast.wuzzuf;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import smile.data.DataFrame;

public interface DAO {
    DataFrame readJobsfromCSV(String filename) throws Exception;
    DataFrame displaySomeData() ;
    Map<String, Integer> dataSummary();
    DataFrame displaySchema();
    DataFrame displaySummary();
    DataFrame cleanData();
    Map countJobsForCompany();
    Map popularJobTitles();
    Map popularAreas();
    Map importantSkills();
    List<List<ClusterObject>> kMeans(int numOfclusters);
    List<Job> toJoblist(DataFrame data);
    List<Job> toFactorizedJoblist(DataFrame data);
    List<Schema> toSchemalist(DataFrame data);
    List<Summary> toSummarylist(DataFrame data);
}
