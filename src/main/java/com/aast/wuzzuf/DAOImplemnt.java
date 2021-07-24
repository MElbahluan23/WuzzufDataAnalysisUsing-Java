package com.aast.wuzzuf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.apache.commons.csv.CSVFormat;
import smile.clustering.KMeans;
import smile.clustering.PartitionClustering;
import smile.data.DataFrame;
import smile.data.Tuple;
import smile.data.measure.NominalScale;
import smile.data.vector.IntVector;
import smile.io.Read;


public class DAOImplemnt implements DAO {

    DataFrame jobs;
    public DAOImplemnt() throws Exception {
        jobs = readJobsfromCSV("src/main/resources/Wuzzuf_Jobs.csv");
    }

    @Override
    public DataFrame readJobsfromCSV(String filename) throws Exception {
         DataFrame jobs = Read.csv(filename, CSVFormat.DEFAULT.withDelimiter(',')
                .withHeader("Title","Company","Location","Type","Level","YearsExp","Country","Skills")
                .withSkipHeaderRecord(true));
        return jobs;
    }

    @Override
    public Map<String, Integer> dataSummary()  {
       // return "{Dataset has "+jobs.ncols()+" features and "+jobs.nrows()+" rows}";
        Map<String, Integer> map =  new HashMap<>();
        map.put("col",jobs.ncols());
        map.put("rows",jobs.nrows());
        return map;

    }

    @Override
    public DataFrame displaySomeData() {
        return jobs.slice(0, 10);//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DataFrame displaySchema() {
        return jobs.structure();//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DataFrame displaySummary() {
        return jobs.summary();//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DataFrame cleanData(){
        jobs.omitNullRows();
        List<Tuple> data = jobs.stream().distinct().collect(Collectors.toList());
        jobs = DataFrame.of(data);
        List<Tuple> datafiltred = jobs.stream().filter(t -> !t.get("YearsExp").equals("null Yrs of Exp")).collect(Collectors.toList());
        jobs = DataFrame.of(datafiltred);
        String[] values = jobs.stringVector("YearsExp").toStringArray();
        int[] factorized = new int[values.length];
        for (int i = 0 ; i <values.length ; i++ ){
            factorized[i] = Integer.parseInt(values[i].split("[+\\-]")[0]);
        }
        if(jobs.ncols() == 8){
        jobs = jobs.merge(IntVector.of("MinYearsExp",factorized));
        }
        return jobs;
    }

    @Override
    public Map countJobsForCompany(){
    Map<String, Long> countMap = jobs.stream()
	.collect(Collectors.groupingBy(t -> String.valueOf (t.getString("Company")), Collectors.counting()));

    Map<String, Long> groupBySorted = countMap.entrySet().stream().sorted(Entry.<String, Long>comparingByValue().reversed())
	.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));

    return groupBySorted;
    }

    @Override
    public Map popularJobTitles(){
    Map<String, Long> countMap = jobs.stream()
	.collect(Collectors.groupingBy(t -> String.valueOf (t.getString("Title")), Collectors.counting()));

    Map<String, Long> groupBySorted = countMap.entrySet().stream().sorted(Entry.<String, Long>comparingByValue().reversed())
	.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));

    return groupBySorted;
    }

    @Override
    public Map popularAreas(){
    Map<String, Long> countMap = jobs.stream()
	.collect(Collectors.groupingBy(t -> String.valueOf (t.getString("Location")), Collectors.counting()));

    Map<String, Long> groupBySorted = countMap.entrySet().stream().sorted(Entry.<String, Long>comparingByValue().reversed())
	.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));

    return groupBySorted;
    }

    @Override
    public Map importantSkills(){
        String[] values = jobs.stringVector("Skills").toStringArray();
        Map<String,Integer> skills =  new HashMap<String, Integer>();
        for(String i : values){
            String[] splitted = i.split(",");
            for(String j : splitted){
                String skill = j.trim();
                if(skills.containsKey(skill))
                    skills.put(skill, skills.get(skill)+1);
                else
                    skills.put(skill, 1);
            }
        }
        
        skills = skills.entrySet().stream().sorted(Entry.<String, Integer>comparingByValue().reversed())
	.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));
         return skills;
    }

    @Override
    public List<List<ClusterObject>> kMeans(int numOfClusters){
        DataFrame df = jobs.select("Company","Title");
        DataFrame dfTrain = DataFrame.of(IntVector.of("Company", Encoder(df, "Company")));
        dfTrain = dfTrain.merge(IntVector.of("Title", Encoder(df, "Title")));

        double[][] finalDfTrain = dfTrain.toArray();
        KMeans clusters = PartitionClustering.run(20, () -> KMeans.fit(finalDfTrain, numOfClusters));
        List<List<ClusterObject>> arrays = new ArrayList<>();
        for (int i = 0 ; i < numOfClusters ; i++){
            List<ClusterObject> objects = new ArrayList<>();
            arrays.add(objects);
        }
        for (int i = 0 ; i < clusters.y.length ; i++){
            arrays.get(clusters.y[i]).add(new ClusterObject(finalDfTrain[i][0],finalDfTrain[i][1]));
        }
        return arrays;
    }

    @Override
    public List<Job> toJoblist(DataFrame data){
        List<Job> allData = new ArrayList<>();
        for (Tuple t : data.stream().collect(Collectors.toList())) {
            Job wuzzufJob = new Job(
                    t.get("Title").toString(),
                    t.get("Company").toString(),
                    t.get("Location").toString(),
                    t.get("Type").toString(),
                    t.get("Level").toString(),
                    t.get("YearsExp").toString(),
                    t.get("Country").toString(),
                    t.get("Skills").toString());
            allData.add(wuzzufJob);
        }
        return allData;
    }

    @Override
    public List<Job> toFactorizedJoblist(DataFrame data){
        List<Job> allData = new ArrayList<>();
        for (Tuple t : data.stream().collect(Collectors.toList())) {
            Job wuzzufJob = new Job(
                    t.get("Title").toString(),
                    t.get("Company").toString(),
                    t.get("Location").toString(),
                    t.get("Type").toString(),
                    t.get("Level").toString(),
                    t.get("YearsExp").toString(),
                    t.get("Country").toString(),
                    t.get("Skills").toString());
            wuzzufJob.setMinExp((int)t.get("MinYearsExp"));
            allData.add(wuzzufJob);
        }
        return allData;
    }

    @Override
    public List<Schema> toSchemalist(DataFrame data){
        List<Schema> allData = new ArrayList<>();
        for (Tuple t : data.stream().collect(Collectors.toList())) {
            Schema schema = new Schema(
                    t.get("Column").toString(),
                    t.get("Type").toString());
            allData.add(schema);
        }
        return allData;
    }

    @Override
    public List<Summary> toSummarylist(DataFrame data){
        List<Summary> allData = new ArrayList<>();
        for (Tuple t : data.stream().collect(Collectors.toList())) {
            Summary summary = new Summary(
                    t.get("column").toString(),
                    (long) t.get("count"),
                    (double)t.get("min"),
                    (double)t.get("avg"),
                    (double)t.get("max")
            );
            allData.add(summary);
        }
        return allData;
    }

    public static int[] Encoder(DataFrame df, String columnName) {
        String[] values = df.stringVector(columnName).distinct().toArray(new String[] {});
        int[] classValues = df.stringVector(columnName).factorize(new NominalScale(values)).toIntArray();
        return classValues;
    }

}
