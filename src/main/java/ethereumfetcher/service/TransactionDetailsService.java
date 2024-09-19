package ethereumfetcher.service;

import ethereumfetcher.persistence.model.TransactionDetails;
import ethereumfetcher.persistence.model.TransactionStatus;
import ethereumfetcher.persistence.model.dto.ResponseTransactionDetails;
import ethereumfetcher.persistence.repository.TransactionDetailsRepository;
import ethereumfetcher.properties.EthereumFetcherProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.rlp.*;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionDetailsService {

    private final TransactionDetailsRepository transactionDetailsRepository;
    private final EthereumFetcherProperties properties;
    private Web3j web3jClient;

    @PostConstruct
    public void postConstruct() {
        this.web3jClient = Web3j.build(
                new HttpService(properties.getEthereumNodeUrl()));
    }

    public List<ResponseTransactionDetails> getTransactionsByHash(List<String> transactionHashes) {
        List<ResponseTransactionDetails> transactions = new ArrayList<>();
        for (String transactionHash : transactionHashes) {
            TransactionDetails transactionDetails =
                    transactionDetailsRepository.findByTransactionHash(transactionHash);
            if (transactionDetails != null) {
                transactions.add(new ResponseTransactionDetails(transactionDetails));
            } else {
                try {
                    Optional<Transaction> transaction = web3jClient
                            .ethGetTransactionByHash(transactionHash).send().getTransaction();
                    Optional<TransactionReceipt> receipt = web3jClient
                            .ethGetTransactionReceipt(transactionHash).send().getTransactionReceipt();

                    if (transaction.isPresent() && receipt.isPresent()) {
                        transactionDetails = createTransactionDetails(
                                transaction.get(), receipt.get());
                        transactionDetailsRepository.save(transactionDetails);
                        transactions.add(new ResponseTransactionDetails(transactionDetails));
                    } else {
                        throw new IllegalArgumentException("Failed to fetch details for transaction with hash: "
                                + transactionHash + ". The cause might be invalid hash.");
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Failed to fetch details for transaction with hash: "
                            + transactionHash);
                }
            }
        }
        return transactions;
    }

    public List<ResponseTransactionDetails> getAllTransactions() {
        return transactionDetailsRepository.findAllResponseTransactionDetails();
    }

    public List<ResponseTransactionDetails> getTransactionsByRlpList(String rlphex) {
        List<RlpType> transactions;
        try {
            RlpList decodedRlphex = RlpDecoder.decode(Numeric.hexStringToByteArray(rlphex));
            transactions = ((RlpList) (decodedRlphex.getValues().get(0))).getValues();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to decode the RLP encoded transactions.");
        }

        List<String> transactionHashes = new ArrayList<>();
        for (RlpType transaction : transactions) {
            transactionHashes.add(new String(((RlpString) transaction).getBytes()));
        }
        return getTransactionsByHash(transactionHashes);
    }

    private TransactionDetails createTransactionDetails(
            Transaction transaction, TransactionReceipt receipt) {
        return new TransactionDetails(
                transaction.getHash(),
                TransactionStatus.fromValue(Numeric.decodeQuantity(
                        receipt.getStatus()).shortValueExact()),
                transaction.getBlockHash(),
                transaction.getBlockNumber(),
                transaction.getFrom(),
                transaction.getTo(),
                receipt.getContractAddress(),
                receipt.getLogs().size(),
                transaction.getInput(),
                transaction.getValue().toString()
        );
    }
}
