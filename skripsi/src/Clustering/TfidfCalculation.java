package Clustering;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class TfidfCalculation {

    SortedSet<String> wordList = new TreeSet(String.CASE_INSENSITIVE_ORDER);
    public ArrayList<String> fileNameList = new ArrayList<String>();
    //List<String> al=new ArrayList<String>();

    //====================== preprocessing ====================

    //cek input number atau bukan
    public  boolean isDigit(String input)
    {
        String regex = "(.)*(\\d)(.)*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        boolean isMatched = matcher.matches();
        if (isMatched) {
            return true;
        }
        return false;
    }

    //preprocessing
    public  String cleanseInput(String input)
    {
        String newStr = input.replaceAll("[, . : ;\"]", "");
        newStr = newStr.replaceAll("\\p{P}","");
        newStr = newStr.replaceAll("\t","");
        return newStr;
    }


    // baca input dari file ke hashmap
    public  HashMap<String, Integer> getTermsFromFile(String Filename,int count,File folder) {
        HashMap<String,Integer> WordCount = new HashMap<String,Integer>();
        BufferedReader reader = null;
        HashMap<String, Integer> finalMap = new HashMap<>();
        try
        {
            reader = new BufferedReader(new FileReader(Filename));
            String line = reader.readLine();
            while(line!=null)
            {
                String[] words = line.toLowerCase().split(" ");
                for(String term : words)
                {
                    //bersih2 input
                    term = cleanseInput(term);
                    //abaikan angka
                    if(isDigit(term))
                    {
                        continue;
                    }
                    if(term.length() == 0)
                    {
                        continue;
                    }
                    wordList.add(term);
                    if(WordCount.containsKey(term))
                    {
                        WordCount.put(term,WordCount.get(term)+1);
                    }
                    else
                    {
                        WordCount.put(term,1);
                    }
                }
                line = reader.readLine();
            }

            Map<String, Integer> treeMap = new TreeMap<>(WordCount);
            finalMap = new HashMap<String, Integer>(treeMap);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return finalMap;
    }


    //hitung idf
    public HashMap<String,Double> calculateInverseDocFrequency(DocumentProperties [] docProperties)
    {

        HashMap<String,Double> InverseDocFreqMap = new HashMap<>();
        int size = docProperties.length;
        double wordCount ;
        for (String word : wordList) {
            wordCount = 0;
            for(int i=0;i<size;i++)
            {
                HashMap<String,Integer> tempMap = docProperties[i].getWordCountMap();
                if(tempMap.containsKey(word))
                {
                    wordCount++;
                    continue;
                }
            }
            double temp = size/ wordCount;
            double idf =  Math.log10(temp);
            idf=idf+1;
            InverseDocFreqMap.put(word,idf);
        }
        return InverseDocFreqMap;
    }

    //==================== /preprocessing ===================



    //======================== tf-idf ===========================
    //hitung term frequency
    public HashMap<String,Double> calculateTermFrequency(HashMap<String,Integer>inputMap) {

        HashMap<String ,Double> termFreqMap = new HashMap<>();
        double sum = 0.0;

        //hitung jumlah elemen di hashmap
        for (float val : inputMap.values()) {
            sum += val;
        }


        //buat hashmap baru berisi nilai TF
        Iterator it = inputMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            double tf = (Integer)pair.getValue()/ sum;
            termFreqMap.put((pair.getKey().toString()),tf);
        }
        return termFreqMap;
    }

    //============================== /tf-idf =======================



    //============================== main ==========================
    public double[][] calculateMatrix() throws IOException {
     //public double[][] calculateMatrix {
        //System.out.print("Enter path for input files ");
        Scanner scan = new Scanner(System.in);
        int count = 0;
        TfidfCalculation TfidfObj = new TfidfCalculation();

        //File folder = new File(scan.nextLine());
        File folder = new File("C:\\Users\\Windows\\Desktop\\bbc\\real");
        //loop file di path
        File[] listOfFiles = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return !file.isHidden();
            }
        });
        //
        int noOfDocs = listOfFiles.length;
        ArrayList<String> fileList = new ArrayList<String>();
        // document
        DocumentProperties [] docProperties = new DocumentProperties[noOfDocs];


        // get wordcount dari file dan hitung term frequency
        for (File file : listOfFiles) {
            if (file.isFile()) {
                //fileNameList[count]=file.getName();
                fileList.add(file.getName());
                docProperties[count] = new DocumentProperties();
                HashMap<String,Integer> wordCount = TfidfObj.getTermsFromFile(file.getAbsolutePath(),count, folder);
                docProperties[count].setWordCountMap(wordCount);
                HashMap<String,Double> termFrequency = TfidfObj.calculateTermFrequency(docProperties[count].DocWordCounts);
                docProperties[count].setTermFreqMap(termFrequency);
                count++;
            }
        }
        //hitung idf
        HashMap<String,Double> inverseDocFreqMap = TfidfObj.calculateInverseDocFrequency(docProperties);
        List<double[]> VectorList = new ArrayList<double[]>();
        //hitung tf-idf
        count = 0;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                HashMap<String,Double> tfIDF = new HashMap<>();
                double tfIdfValue = 0.0;
                double idfVal = 0.0;
                HashMap<String,Double> tf = docProperties[count].getTermFreqMap();
                Iterator itTF = tf.entrySet().iterator();
                while (itTF.hasNext()) {
                    Map.Entry pair = (Map.Entry)itTF.next();
                    double tfVal  = (Double)pair.getValue() ;
                    if(inverseDocFreqMap.containsKey((String)pair.getKey()))
                    {
                        idfVal = inverseDocFreqMap.get((String)pair.getKey());
                    }
                    tfIdfValue = tfVal *idfVal;
                    tfIDF.put((pair.getKey().toString()),tfIdfValue);
                }
                int fileNameNumber = (count+1);
                //System.out.println("Document "+fileNameNumber);
                this.fileNameList=fileList;
                TfidfObj.print(tfIDF);
                VectorList.add(TfidfObj.calculateVector(tfIDF));
                System.out.println("=================");
                count++;
               

            }

        }
        //TfidfObj.printVector(VectorList);
       // TfidfObj.printWord();
        //System.out.println(TfidfObj.calculateCosine(VectorList.get(0),VectorList.get(0)));
        double[][] result=TfidfObj.createDistMatrix(VectorList);
        return result;
        //TfidfObj.printMatrix(result);
    }




    //========================= /main =====================



    //=====================return list nama file============


    public String[] returnFileNames(){

        String[] nameList=new String[this.fileNameList.size()];
        nameList=this.fileNameList.toArray(nameList);
        return nameList;

    }

    //======================================================

    //======================== cosine similarity============

    public static double cosineSimilarity(double[] vectorA, double[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    //====================== /cosine ====================


    //====================== calculate vector ===========
    public double[] calculateVector(HashMap<String,Double> tfidf){
        double[] documentvector=new double[wordList.size()];
        Iterator<String> it = wordList.iterator();

        int count=0;
        while(it.hasNext() ) {
            String current = it.next();
            if(tfidf.containsKey(current)){
                documentvector[count]=tfidf.get(current);
                documentvector[count]=documentvector[count];
            } else{
                documentvector[count]=0.0;
            }
            count++;
        }
        return documentvector;
    }

    public double calculateCosine(double[] A, double[] B){
            double dotProduct = 0.0;
            double normA = 0.0;
            double normB = 0.0;
            for (int i = 0; i < A.length; i++) {
                dotProduct += A[i] * B[i];
                normA += Math.pow(A[i], 2);
                normB += Math.pow(B[i], 2);
            }
            return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
        }


        public double[][] createDistMatrix(List<double[]> vectorList){

        double[][] distmatrix = new double[vectorList.size()][vectorList.size()];


            for (int i = 0; i < vectorList.size() ; i++) {
                for(int j=0;j<vectorList.size();j++){
                     double result = calculateCosine(vectorList.get(i),vectorList.get(j));
                     result=result;
                    distmatrix[i][j]=result;
                    distmatrix[j][i]=result;
                }
            }
            return distmatrix;
        }

    //==================== /calculate vector ==================


    //=================== buat debug ========================
    public void print(HashMap<String,Double> result){
        for (Map.Entry<String, Double> keymap : result.entrySet()) {
            System.out.println(keymap.getKey());
            System.out.println(keymap.getValue());
        }

    }


    private void printMatrix(double[][] res){
        System.out.println("=============");
        for(int i=0; i<res.length; i++) {

            for(int j=0; j<res[i].length; j++) {
                System.out.print(res[i][j]+" ");
            }
            System.out.println("");
        }
        System.out.println("=============");

    }

    private void printVector(List<double[]> vectorList) {
        for (double[] vector : vectorList) {
            for (int i = 0; i < vector.length; i++) {
                System.out.println(vector[i]);
            }
            System.out.println("==============");
        }
    }

    public void printWord(){
        Iterator<String> it = wordList.iterator();
        while(it.hasNext()){
            System.out.println(it.next());
        }

    }
    //=================== /buat debug ========================


}



/*
kelas berisi detail dari dokumen seperti word frequency untuk setiap term dan term frequency untuk setiap term dokumen
This class contains details such as the word frequency count for each term and term frequency for each term of the document.
 */
class DocumentProperties{


    private HashMap<String,Double> termFreqMap ;
     HashMap<String,Integer> DocWordCounts;



    public HashMap<String,Double> getTermFreqMap()
    {

        return termFreqMap;
    }

    public HashMap<String,Integer> getWordCountMap()
    {

        return DocWordCounts;
    }

    public void setTermFreqMap(HashMap<String,Double> inMap)
    {

        termFreqMap = new HashMap<String, Double>(inMap);
    }


    public void setWordCountMap(HashMap<String,Integer> inMap)
    {

        DocWordCounts =new HashMap<String, Integer>(inMap);
    }


}