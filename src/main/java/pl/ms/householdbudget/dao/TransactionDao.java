package pl.ms.householdbudget.dao;

import org.springframework.stereotype.Repository;
import pl.ms.householdbudget.model.Transaction;
import pl.ms.householdbudget.model.TransactionType;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TransactionDao {
    private Connection connection;

    public TransactionDao() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/household_budget",
                    "root", "admin");
        } catch (SQLException e) {
            throw new RuntimeException("Nie udało się połączyć z bazą", e);
        }
    }

    public Optional<Transaction> updateTransaction(Transaction transaction) {
        final String sql = "UPDATE transaction SET type =?, description =?, amount =?, date =? WHERE id =?";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1, transaction.getType().name());
            preparedStatement.setString(2, transaction.getDescription());
            preparedStatement.setDouble(3, transaction.getAmount());
            Date date = Date.valueOf(transaction.getDate());
            preparedStatement.setDate(4, date);
            preparedStatement.setInt(5, transaction.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return findTransactionById(transaction.getId());
    }

    public Optional<Transaction> findTransactionById(int id) {
        final String sql = "SELECT id, type, description, amount, date FROM transaction WHERE id =?";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int idInDb = resultSet.getInt("id");
                String typeName = resultSet.getString("type");
                TransactionType type = TransactionType.valueOf(typeName);
                String description = resultSet.getString("description");
                double amount = resultSet.getDouble("amount");
                Date dateInDb = resultSet.getDate("date");
                LocalDate date = dateInDb.toLocalDate();
                return Optional.of(new Transaction(idInDb, type, description, amount, date));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Błąd przy wyszukiwaniu transakcji po id", e);
        }
        return Optional.empty();
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Nie udało się zamknąć połączenia", e);
        }
    }
    public void saveTransaction(Transaction transaction) {
        final String sqlSave = "INSERT INTO transaction(type, description, amount, date) VALUES (?,?,?,?)";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(sqlSave, Statement.RETURN_GENERATED_KEYS);
        ) {
            preparedStatement.setString(1, transaction.getType().name());
            preparedStatement.setString(2, transaction.getDescription());
            preparedStatement.setDouble(3, transaction.getAmount());
            Date date = Date.valueOf(transaction.getDate());
            preparedStatement.setDate(4, date);
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                transaction.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Nie udało się zapisaĆ transakcji", e);
        }
    }

    public List<Transaction> selectAllTransactions() {
        final String sql = "SELECT id, type, description, amount, date FROM transaction";
        List<Transaction> transactions = new ArrayList<>();
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            getTransactionsList(transactions, preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }

    public List<Transaction> filterByTransactionType(TransactionType type) {
        final String sql = "SELECT id, type, description, amount, date FROM transaction WHERE type= ?";
        List<Transaction> transactions = new ArrayList<>();
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1, type.name());
            getTransactionsList(transactions, preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }

    private void getTransactionsList(List<Transaction> transactions, PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int idInDb = resultSet.getInt("id");
            String typeName = resultSet.getString("type");
            TransactionType typeInDb = TransactionType.valueOf(typeName);
            String description = resultSet.getString("description");
            double amount = resultSet.getDouble("amount");
            Date dateInDb = resultSet.getDate("date");
            LocalDate date = dateInDb.toLocalDate();
            transactions.add(new Transaction(idInDb, typeInDb, description, amount, date));
        }
    }
    public boolean deleteTransaction(Transaction transaction) {
        final String sql = "DELETE FROM transaction WHERE id=?";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, transaction.getId());
            int updatedRows = preparedStatement.executeUpdate();
            return (updatedRows != 0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
