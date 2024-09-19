package ethereumfetcher.persistence.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigInteger;

@Data
@Entity
@Table(name = "transactions")
@NoArgsConstructor
public class TransactionDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String transactionHash;

    @Enumerated(EnumType.ORDINAL)
    private TransactionStatus transactionStatus;

    private String blockHash;

    private BigInteger blockNumber;

    @Column(name = "\"from\"")
    private String from;

    @Column(name = "\"to\"")
    private String to;

    private String contractAddress;

    private Integer logsCount;

    @Column(columnDefinition = "TEXT")
    private String input;

    private String value;

    public TransactionDetails(String transactionHash, TransactionStatus transactionStatus,
                              String blockHash, BigInteger blockNumber, String from,
                              String to, String contractAddress, Integer logsCount,
                              String input, String value) {
        this.transactionHash = transactionHash;
        this.transactionStatus = transactionStatus;
        this.blockHash = blockHash;
        this.blockNumber = blockNumber;
        this.from = from;
        this.to = to;
        this.contractAddress = contractAddress;
        this.logsCount = logsCount;
        this.input = input;
        this.value = value;
    }
}
