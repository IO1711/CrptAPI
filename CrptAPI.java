import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import io.github.bucket4j.*;
import com.fasterxml.jackson.databind.*;    
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;

public class CrptAPI{
    private final Bucket bucket;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiUrl = "https://ismp.crpt.ru/api/v3/lk/documents/create";

    public CrptAPI(TimeUnit timeUnit, int requestLimit){
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        long durationInSeconds = timeUnit.toSeconds(1);
        Bandwidth bandwidth = Bandwidth.classic(requestLimit, Refill.greedy(requestLimit, Duration.ofSeconds(durationInSeconds)));
        this.bucket = Bucket.builder()
            .addLimit(bandwidth)
            .build();
    }

    public void createDocument(Document document, String signature) throws Exception{
        if(bucket.tryConsume(1)){
            String documentAsJson = objectMapper.writeValueAsString(document);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(apiUrl))
                .header("Content-Type", "application/json")
                .header("Signature", signature)
                .POST(HttpRequest.BodyPublishers.ofString(documentAsJson))
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 200){
                System.out.println("Document creation success");
            }
            else{
                System.out.println("Document creation failed");
            }
        }
        else{
            System.out.println("Rate limit reached. Try again later.");
        }


    }

    
}

class Document{
    private Description description;
    private String doc_id;
    private String doc_status;
    private String doc_type;
    private boolean importRequest;
    private String owner_inn;
    private String participant_inn;
    private String producer_inn;
    private String production_date;
    private String production_type;
    private List<Product> products;
    private String reg_date;
    private String reg_number;


    public Description getDescription() {
        return this.description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public String getDoc_id() {
        return this.doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public String getDoc_status() {
        return this.doc_status;
    }

    public void setDoc_status(String doc_status) {
        this.doc_status = doc_status;
    }

    public String getDoc_type() {
        return this.doc_type;
    }

    public void setDoc_type(String doc_type) {
        this.doc_type = doc_type;
    }

    public boolean isImportRequest() {
        return this.importRequest;
    }

    public boolean getImportRequest() {
        return this.importRequest;
    }

    public void setImportRequest(boolean importRequest) {
        this.importRequest = importRequest;
    }

    public String getOwner_inn() {
        return this.owner_inn;
    }

    public void setOwner_inn(String owner_inn) {
        this.owner_inn = owner_inn;
    }

    public String getParticipant_inn() {
        return this.participant_inn;
    }

    public void setParticipant_inn(String participant_inn) {
        this.participant_inn = participant_inn;
    }

    public String getProducer_inn() {
        return this.producer_inn;
    }

    public void setProducer_inn(String producer_inn) {
        this.producer_inn = producer_inn;
    }

    public String getProduction_date() {
        return this.production_date;
    }

    public void setProduction_date(String production_date) {
        this.production_date = production_date;
    }

    public String getProduction_type() {
        return this.production_type;
    }

    public void setProduction_type(String production_type) {
        this.production_type = production_type;
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getReg_date() {
        return this.reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getReg_number() {
        return this.reg_number;
    }

    public void setReg_number(String reg_number) {
        this.reg_number = reg_number;
    }
    

    
}

class Description{
    private String participantInn;

    public String getParticipantInn() {
        return this.participantInn;
    }

    public void setParticipantInn(String participantInn) {
        this.participantInn = participantInn;
    }

    

}

class Product{
    private String certificate_document;
    private String certificate_document_date;
    private String certificate_document_number;
    private String owner_inn;
    private String producer_inn;
    private String production_date;
    private String tnved_code;
    private String uit_code;
    private String uitu_code;

    public String getCertificate_document() {
        return this.certificate_document;
    }

    public void setCertificate_document(String certificate_document) {
        this.certificate_document = certificate_document;
    }

    public String getCertificate_document_date() {
        return this.certificate_document_date;
    }

    public void setCertificate_document_date(String certificate_document_date) {
        this.certificate_document_date = certificate_document_date;
    }

    public String getCertificate_document_number() {
        return this.certificate_document_number;
    }

    public void setCertificate_document_number(String certificate_document_number) {
        this.certificate_document_number = certificate_document_number;
    }

    public String getOwner_inn() {
        return this.owner_inn;
    }

    public void setOwner_inn(String owner_inn) {
        this.owner_inn = owner_inn;
    }

    public String getProducer_inn() {
        return this.producer_inn;
    }

    public void setProducer_inn(String producer_inn) {
        this.producer_inn = producer_inn;
    }

    public String getProduction_date() {
        return this.production_date;
    }

    public void setProduction_date(String production_date) {
        this.production_date = production_date;
    }

    public String getTnved_code() {
        return this.tnved_code;
    }

    public void setTnved_code(String tnved_code) {
        this.tnved_code = tnved_code;
    }

    public String getUit_code() {
        return this.uit_code;
    }

    public void setUit_code(String uit_code) {
        this.uit_code = uit_code;
    }

    public String getUitu_code() {
        return this.uitu_code;
    }

    public void setUitu_code(String uitu_code) {
        this.uitu_code = uitu_code;
    }

}


    
