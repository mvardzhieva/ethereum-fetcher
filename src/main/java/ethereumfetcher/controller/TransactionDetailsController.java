package ethereumfetcher.controller;

import ethereumfetcher.persistence.model.dto.ResponseTransactionDetails;
import ethereumfetcher.service.TransactionDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class TransactionDetailsController {

    private final TransactionDetailsService transactionDetailsService;

    @GetMapping("/eth")
    public ResponseEntity<List<ResponseTransactionDetails>> getTransactionsByHash(
            @RequestParam List<String> transactionHashes) {
        List<ResponseTransactionDetails> transactions =
                transactionDetailsService.getTransactionsByHash(transactionHashes);
        return new ResponseEntity<>(transactions, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ResponseTransactionDetails>> getAllTransactions() {
        List<ResponseTransactionDetails> transactions =
                transactionDetailsService.getAllTransactions();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/eth/{rlphex}")
    public ResponseEntity<List<ResponseTransactionDetails>> getTransactionsByRlpList(
            @PathVariable String rlphex) {
        List<ResponseTransactionDetails> transactions =
                transactionDetailsService.getTransactionsByRlpList(rlphex);
        return new ResponseEntity<>(transactions, HttpStatus.CREATED);
    }

}
