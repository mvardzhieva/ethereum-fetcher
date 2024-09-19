package ethereumfetcher.controller;

import ethereumfetcher.persistence.model.TransactionStatus;
import ethereumfetcher.persistence.model.dto.ResponseTransactionDetails;
import ethereumfetcher.service.TransactionDetailsService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigInteger;
import java.util.List;

@ActiveProfiles("test")
@WebMvcTest(TransactionDetailsController.class)
public class TransactionDetailsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionDetailsService transactionDetailsServiceMock;

    private final List<ResponseTransactionDetails> transactions =
            List.of(new ResponseTransactionDetails(
                    "0xcbc920e7bb89cbcb540a469a16226bf1057825283ab8eac3f45d00811eef8a64",
            TransactionStatus.SUCCESS,
            "0xc5a3664f031da2458646a01e18e6957fd1f43715524d94b7336a004b5635837d",
            BigInteger.valueOf(5702816),
            "0xd5e6f34bbd4251195c03e7bf3660677ed2315f70",
            "0x4c16d8c078ef6b56700c1be19a336915962df072",
            null,
            1,
            "0x6a627842000000000000000000000000d5e6f34bbd4251195c03e7bf3660677ed2315f70",
            "0"
    ));

    private final String expectedTransactionsOutput = """
            [
                {
                    "transactionHash": "0xcbc920e7bb89cbcb540a469a16226bf1057825283ab8eac3f45d00811eef8a64",
                    "transactionStatus": 1,
                    "blockHash": "0xc5a3664f031da2458646a01e18e6957fd1f43715524d94b7336a004b5635837d",
                    "blockNumber": 5702816,
                    "from": "0xd5e6f34bbd4251195c03e7bf3660677ed2315f70",
                    "to": "0x4c16d8c078ef6b56700c1be19a336915962df072",
                    "contractAddress": null,
                    "logsCount": 1,
                    "input": "0x6a627842000000000000000000000000d5e6f34bbd4251195c03e7bf3660677ed2315f70",
                    "value": "0"
                }
            ]""";

    @Test
    public void testGetTransactionsByValidHash() throws Exception {
        when(transactionDetailsServiceMock.getTransactionsByHash(any()))
                .thenReturn(transactions);

        String transactionHash = "0xcbc920e7bb89cbcb540a469a16226bf1057825283ab8eac3f45d00811eef8a64";
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/eth?transactionHashes=" + transactionHash))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(expectedTransactionsOutput));
    }

    @Test
    public void testGetTransactionsByInvalidHash() throws Exception {
        String exceptionMessage = "Failed to fetch details for transaction " +
                "with hash: invalidInput. The cause might be invalid hash.";
        when(transactionDetailsServiceMock.getTransactionsByHash(any()))
                .thenThrow(new IllegalArgumentException(exceptionMessage));

        String invalidInput = "0xasdf1234";
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/eth?transactionHashes=" + invalidInput))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(exceptionMessage));
    }

    @Test
    public void testGetAllTransactions() throws Exception {
        when(transactionDetailsServiceMock.getAllTransactions())
                .thenReturn(transactions);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(expectedTransactionsOutput));

    }

    @Test
    public void testGetTransactionsByValidRlpList() throws Exception {
        when(transactionDetailsServiceMock.getTransactionsByRlpList(anyString()))
                .thenReturn(transactions);

        String rlphex = "f844b842307863626339323065376262383963626362353430613436396131363232366266313035373832353238336162386561633366343564303038313165656638613634";
        mockMvc.perform(MockMvcRequestBuilders.get("/api/eth/" + rlphex))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(expectedTransactionsOutput));
    }

    @Test
    public void testGetTransactionsByInvalidRlpList() throws Exception {
        String exceptionMessage = "Failed to decode the RLP encoded transactions.";
        when(transactionDetailsServiceMock.getTransactionsByRlpList(anyString()))
                .thenThrow(new IllegalArgumentException(exceptionMessage));

        String invalidRlpEncoding = "fasdf1234";
        mockMvc.perform(MockMvcRequestBuilders.get("/api/eth/" + invalidRlpEncoding))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(exceptionMessage));
    }
}
