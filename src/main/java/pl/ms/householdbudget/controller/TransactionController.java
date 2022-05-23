package pl.ms.householdbudget.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.ms.householdbudget.dao.TransactionDao;
import pl.ms.householdbudget.model.Transaction;
import pl.ms.householdbudget.model.TransactionType;

import java.util.List;
import java.util.Optional;

@Controller
class TransactionController {
    private final TransactionDao transactionDao;

    public TransactionController(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("mode", "add");
        model.addAttribute("transactionTypes", TransactionType.values());
        return "addOrEdit";
    }

    @PostMapping("/add")
    public String saveTransaction(Transaction transaction, Model model) {
        transactionDao.saveTransaction(transaction);
        model.addAttribute("transaction", transaction);
        model.addAttribute("mode", "add");
        return "success";
    }

    @GetMapping("/edit")
    public String editForm(Model model, @RequestParam int id) {
        Optional<Transaction> transactionById = transactionDao.findTransactionById(id);
        if (transactionById.isPresent()) {
            model.addAttribute(transactionById.get());
            model.addAttribute("mode", "edit");
            model.addAttribute("transactionTypes", TransactionType.values());
            return "addOrEdit";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/edit")
    public String updateTransaction(Transaction transaction, Model model) {
        Optional<Transaction> updatedTransaction = transactionDao.updateTransaction(transaction);
        if (updatedTransaction.isPresent()) {
            model.addAttribute("mode", "edit");
            model.addAttribute("transaction", updatedTransaction.get());
            return "success";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/delete")
    public String deleteForm(Model model, @RequestParam int id) {
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("transactionTypes", TransactionType.values());
        Optional<Transaction> transactionById = transactionDao.findTransactionById(id);
        if (transactionById.isPresent()) {
            model.addAttribute(transactionById.get());
            return "delete";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/delete")
    public String deleteTransaction(Transaction transaction, Model model) {
        String deletedInfo = transaction.toString();
        boolean deleted = transactionDao.deleteTransaction(transaction);
        if (deleted) {
            model.addAttribute("deletedInfo", deletedInfo);
            return "deleted";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/printAll")
    public String printAllTransactions(Model model, @RequestParam String mode) {
        List<Transaction> transactions = transactionDao.selectAllTransactions();
        model.addAttribute("transactions", transactions);
        model.addAttribute("mode", mode);
        return "printAllTransactions";
    }

    @GetMapping("/print")
    public String printTransactionsByType(Model model, @RequestParam String type) {
        List<Transaction> transactions = null;
        if (type.equalsIgnoreCase(TransactionType.EARNING.name())) {
            transactions = transactionDao.filterByTransactionType(TransactionType.EARNING);
        } else if (type.equalsIgnoreCase(TransactionType.EXPENSE.name())) {
            transactions = transactionDao.filterByTransactionType(TransactionType.EXPENSE);
        }
        model.addAttribute("transactionType", type);
        model.addAttribute("transactions", transactions);
        model.addAttribute("mode", "edit");
        return "printTransactions";
    }
}
