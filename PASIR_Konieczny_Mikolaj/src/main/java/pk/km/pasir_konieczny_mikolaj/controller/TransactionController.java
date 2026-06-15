package pk.km.pasir_konieczny_mikolaj.controller;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pk.km.pasir_konieczny_mikolaj.dto.TransactionDTO;
import pk.km.pasir_konieczny_mikolaj.model.Transaction;
import pk.km.pasir_konieczny_mikolaj.service.TransactionService;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    //@Autowired
    //private TransactionRepository transactionRepository;

    //@Autowired
    //private TransactionService transactionService;

    private final TransactionService  transactionService;

    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() throws AccessDeniedException {
        //List<Transaction> transactions = transactionRepository.findAll();
        //return ResponseEntity.ok(transactions);
        return  ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id){
        //Transaction transaction = transactionRepository.findById(id)
        //        .orElseThrow(() -> new RuntimeException("Transaction not Found with id: " + id));
        //transaction.setAmount(transactionDetails.getAmount());
        //transaction.setType(transactionDetails.getType());
        //transaction.setTags(transactionDetails.getTags());
        //transaction.setNotes(transactionDetails.getNotes());

        //Transaction updatedTransatcion = transactionRepository.save(transaction);
        //return ResponseEntity.ok(updatedTransatcion);

        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, @Valid @RequestBody TransactionDTO transactionDTO) throws AccessDeniedException {
        Transaction updatedTransaction = transactionService.updateTransaction(id, transactionDTO);
        return ResponseEntity.ok(updatedTransaction);
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO) throws AccessDeniedException {
        Transaction newTransaction = transactionService.createTransaction(transactionDTO);
        return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
