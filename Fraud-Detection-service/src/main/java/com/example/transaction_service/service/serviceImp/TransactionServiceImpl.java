package com.example.transaction_service.service.serviceImp;

import com.example.transaction_service.dto.TransactionDto;
import com.example.transaction_service.entity.BankAccount;
import com.example.transaction_service.entity.Transaction;
import com.example.transaction_service.entity.User;
import com.example.transaction_service.enumeration.TransactionStatus;
import com.example.transaction_service.enumeration.TypeTransaction;
import com.example.transaction_service.exception.NotFoundException;
import com.example.transaction_service.mapper.ITransactionMapper;
import com.example.transaction_service.repository.IBankAccountRepository;
import com.example.transaction_service.repository.ITransactionRepository;
import com.example.transaction_service.repository.IUserRepository;
import com.example.transaction_service.service.ITransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TransactionServiceImpl  implements ITransactionService {
    @Autowired
    ITransactionRepository transactionRepository;
    @Autowired
    ITransactionMapper transactionMapper;
    @Autowired
    IBankAccountRepository bankAccountRepository;
    @Autowired
    IUserRepository userRepository;

    @Override
    public List<TransactionDto> getAllTransaction() {
        List<Transaction> transactions =transactionRepository.findAll();
        return transactionMapper.toDto(transactions);
    }

    @Override
    public TransactionDto getTransactionById(Long id) {
        Optional<Transaction> transaction =transactionRepository.findById(id);
        if (transaction.isPresent())
        {
           return transactionMapper.toDto(transaction.get());
        }
        else {
            throw new NotFoundException("Transaction with ID " + id + " not found");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TransactionDto createTransaction(TransactionDto transactionDto) {
        // Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = null;
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            email = jwt.getClaim("email"); // 👈 Assurez-vous que 'email' est présent dans le token
        }

        if (email == null) {
            throw new RuntimeException("Email introuvable dans le token.");
        }

        log.info("Connected user email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found lala"));



        // Récupérer le compte source (debit)
        List<BankAccount> senderAccounts = bankAccountRepository.findByUser_UserId(user.getUserId());
        if (senderAccounts.isEmpty()) {
            throw new NotFoundException("Sender's bank account not found");
        }
        BankAccount senderAccount = senderAccounts.get(0);

        // Vérifier le solde
        if (senderAccount.getBalance().compareTo(transactionDto.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // Récupérer le compte du bénéficiaire (crédit)
        BankAccount receiverAccount = bankAccountRepository.findById(transactionDto.getBankAccountId())
                .orElseThrow(() -> new NotFoundException("Receiver bank account not found"));

        // 1. Débit : créer transaction pour l'utilisateur connecté
        Transaction debitTransaction = new Transaction();
        debitTransaction.setAmount(transactionDto.getAmount());
        debitTransaction.setCurrency(transactionDto.getCurrency());
        debitTransaction.setCountry(transactionDto.getCountry());
        debitTransaction.setTransactionDate(transactionDto.getTransactionDate());
        debitTransaction.setBankAccount(senderAccount);
        debitTransaction.setTransactionStatus(TransactionStatus.VALID);
        debitTransaction.setTypeTransaction(TypeTransaction.TRANSFER);
        transactionRepository.save(debitTransaction);

        // Mise à jour du solde expéditeur
        senderAccount.setBalance(senderAccount.getBalance().subtract(transactionDto.getAmount()));
        bankAccountRepository.save(senderAccount);

        // 2. Crédit : créer transaction pour le bénéficiaire
        Transaction creditTransaction = new Transaction();
        creditTransaction.setAmount(transactionDto.getAmount());
        creditTransaction.setCurrency(transactionDto.getCurrency());
        creditTransaction.setCountry(transactionDto.getCountry());
        creditTransaction.setTransactionDate(transactionDto.getTransactionDate());
        creditTransaction.setBankAccount(receiverAccount);
        creditTransaction.setTransactionStatus(TransactionStatus.VALID);
        creditTransaction.setTypeTransaction(TypeTransaction.TRANSFER);
        transactionRepository.save(creditTransaction);

        // Mise à jour du solde bénéficiaire
        receiverAccount.setBalance(receiverAccount.getBalance().add(transactionDto.getAmount()));
        bankAccountRepository.save(receiverAccount);

        return transactionMapper.toDto(debitTransaction);
    }


//    @Override
//    public TransactionDto createTransaction(TransactionDto transactionDto) {
//
//        //get user conected
//        // check amount >  transactiondto.getAmout if throw exception impossible
//        // new trasaction lel user conetcte
//        // sold = sold - transactionDto.getAmount
//
//
//        Transaction transaction = transactionMapper.toEntity(transactionDto);
//      //  sold = sold + transactionDto.getAmount
//        BankAccount bankAccount = bankAccountRepository.findById(transactionDto.getBankAccountId())
//                .orElseThrow(() -> new NotFoundException("Bank account not found with ID " + transactionDto.getBankAccountId()));
//        transaction.setBankAccount(bankAccount);
//
//        transaction.setTransactionStatus(TransactionStatus.VALID);
//        transaction.setTypeTransaction(TypeTransaction.TRANSFER);
//        transaction = transactionRepository.save(transaction);
//        return transactionMapper.toDto(transaction);
//    }


    @Override
    public TransactionDto updateTransaction(TransactionDto transactionDto, Long id) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(id);
        if (!optionalTransaction.isPresent()) {
            throw new NotFoundException("Transaction with ID " + id + " not found");
        }

        Transaction transaction = optionalTransaction.get();
        transaction.setTransactionDate(transactionDto.getTransactionDate());
        transaction.setTransactionStatus(transactionDto.getTransactionStatus());
        transaction.setTypeTransaction(transactionDto.getTypeTransaction());
        transaction.setCountry(transactionDto.getCountry());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setCurrency(transactionDto.getCurrency());

        return transactionMapper.toDto(transactionRepository.save(transaction));


    }

    @Override
    public void deleteTransaction(Long id) {

    }

//    public TransactionDto updateTransactionStatus(Long transactionId, boolean isFraudulent) {
//        Transaction transaction = transactionRepository.findById(transactionId)
//                .orElseThrow(() -> new NotFoundException("Transaction non trouvée avec l'ID " + transactionId));
//
//        if (isFraudulent) {
//
//            transaction.setTransactionStatus(TransactionStatus.SUSPICIOUS);
//        } else {
//            transaction.setTransactionStatus(TransactionStatus.VALID);
//        }
//
//        transaction = transactionRepository.save(transaction);
//        return transactionMapper.toDto(transaction);
//    }
}
