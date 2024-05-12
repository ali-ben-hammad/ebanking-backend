package ma.enset.ebankingBackend.dtos;


import lombok.Data;

@Data
public class CurrentAccountDTO extends BankAccountDTO {
    private double overDraft;
}
