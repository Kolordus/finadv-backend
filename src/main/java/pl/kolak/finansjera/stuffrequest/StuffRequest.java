package pl.kolak.finansjera.stuffrequest;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

import static pl.kolak.finansjera.utils.FinanceAppStrings.PAU_OR_JACK_REGEX;

@Document
public class StuffRequest {

    @Id
    @NotBlank
    private String date;

    @Pattern(regexp = PAU_OR_JACK_REGEX, message = "Peron name is not valid! Should be either pau or jack")
    private String personName;

    @NotBlank
    private String operationName;
    
    public StuffRequest(String date, String personName, String operationName) {
        this.date = date;
        this.personName = personName;
        this.operationName = operationName;
    }

    // hibernate
    public StuffRequest() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }
    // hibernate
    
    @Override
    public String toString() {
        return "StuffRequest{" +
                "date='" + date + '\'' +
                ", personName='" + personName + '\'' +
                ", operationName='" + operationName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StuffRequest that = (StuffRequest) o;
        return Objects.equals(date, that.date) && Objects.equals(personName, that.personName) && Objects.equals(operationName, that.operationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, personName, operationName);
    }

    public void setUpdatedValues(StuffRequest request) {

    }
}
