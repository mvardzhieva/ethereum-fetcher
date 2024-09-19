package ethereumfetcher.persistence.model.dto;

import ethereumfetcher.persistence.model.TransactionDetails;
import ethereumfetcher.persistence.model.TransactionStatus;
import lombok.Data;

import java.math.BigInteger;

@Data
public class ResponseTransactionDetails {
    private String transactionHash;
    private Short transactionStatus;
    private String blockHash;
    private BigInteger blockNumber;
    private String from;
    private String to;
    private String contractAddress;
    private Integer logsCount;
    private String input;
    private String value;

    public ResponseTransactionDetails(TransactionDetails transactionDetails) {
        this.transactionHash = transactionDetails.getTransactionHash();
        this.transactionStatus = transactionDetails.getTransactionStatus().getStatus();
        this.blockHash = transactionDetails.getBlockHash();
        this.blockNumber = transactionDetails.getBlockNumber();
        this.from = transactionDetails.getFrom();
        this.to = transactionDetails.getTo();
        this.contractAddress = transactionDetails.getContractAddress();
        this.logsCount = transactionDetails.getLogsCount();
        this.input = transactionDetails.getInput();
        this.value = transactionDetails.getValue();
    }

    public ResponseTransactionDetails(String transactionHash, TransactionStatus transactionStatus,
                                      String blockHash, BigInteger blockNumber,
                                      String from, String to, String contractAddress,
                                      Integer logsCount, String input, String value) {
        this.transactionHash = transactionHash;
        this.transactionStatus = transactionStatus.getStatus();
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
