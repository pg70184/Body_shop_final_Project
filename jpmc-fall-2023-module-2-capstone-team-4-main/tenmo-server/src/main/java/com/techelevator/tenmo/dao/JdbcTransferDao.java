package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;
    private int transferStatusID;
    private int transferTypeID;
    private JdbcUserDao userDao;
    public JdbcTransferDao(JdbcTemplate jdbcTemplate, JdbcUserDao userDao){
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
    }


    @Override
    public int transferType(String transferType) {

        String sqlToUpdateStatus = "INSERT INTO transfer_type (transfer_type_desc) \n" +
                "VALUES (?) RETURNING transfer_type_id;";
        try{
            this.transferTypeID = jdbcTemplate.queryForObject(sqlToUpdateStatus, Integer.class, transferType);

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return this.transferTypeID;
    }

    @Override
    public int transferStatus(String transferStatus) {
        String sqlToUpdateStatus = "INSERT INTO transfer_status (transfer_status_desc) " +
                                "VALUES (?) RETURNING transfer_status_id;";
        try{
            this.transferStatusID = jdbcTemplate.queryForObject(sqlToUpdateStatus, Integer.class, transferStatus);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return this.transferStatusID;

    }
    @Override
    public Transfer createTransfer(TransferDto transfer) {
        Transfer newTransfer = null;
        String sqlForNewProject = "INSERT INTO public.transfer( \n" +
                "\t transfer_type_id, transfer_status_id, account_from, account_to, amount) \n" +
                "\tVALUES (?, ?, (SELECT account_id FROM account WHERE user_id = ?), (SELECT account_id FROM account WHERE user_id = ?), ?) RETURNING transfer_id;";
        try {
            Long newTransferID = jdbcTemplate.queryForObject(sqlForNewProject, Long.class, transferTypeID, transferStatusID, transfer.getUserFrom(),
                    transfer.getUserTo(), transfer.getAmount());

            String search = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount \n" +
                    "FROM public.transfer \n" +
                    "WHERE transfer_id = ?;";

            SqlRowSet results = jdbcTemplate.queryForRowSet(search, newTransferID);
            if(results.next()){
                newTransfer = mapToTransfer(results);
            }

            newTransfer.setTransferId(newTransferID);
        } catch (DataIntegrityViolationException e) {
            //throw new DaoException("createDepartment() not implemented");
            throw new DaoException("Data integrity violation", e);
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Invalid Connection", e);
        }
        return newTransfer;
    }



    public void updateSenderBalance (TransferDto transfer) {
        String sqlToGetCurrentBalance = "SELECT balance \n" +
                "FROM account\n" +
                "WHERE user_id = ?";
        String sqlForUpdatingSenderBalance = "UPDATE public.account\n" +
                "\tSET balance=?\n" +
                "\tWHERE user_id=?;";
        //does not execute if sender is attempting to send to himself
        if(transfer.getUserFrom() != transfer.getUserTo()) {
            try {
                //pulls sender's current balance
                BigDecimal currentBalanceOfSender = jdbcTemplate.queryForObject(sqlToGetCurrentBalance, BigDecimal.class, transfer.getUserFrom());
                //checks if the person is trying to send more than they have in their balance
                if(currentBalanceOfSender.compareTo(transfer.getAmount()) >= 0) {
                    BigDecimal newBalanceOfSender = currentBalanceOfSender.subtract(transfer.getAmount());

                    //Update the sender's balance
                    jdbcTemplate.update(sqlForUpdatingSenderBalance, newBalanceOfSender, transfer.getUserFrom());
                }
            } catch (DataIntegrityViolationException e) {
                //throw new DaoException("createDepartment() not implemented");
                throw new DaoException("Data integrity violation", e);
            } catch (CannotGetJdbcConnectionException e) {
                throw new DaoException("Invalid Connection", e);
            }
        }
    }

    public void updateReceiverBalance (TransferDto transferDto) {
        String sqlToGetCurrentBalance = "SELECT balance \n" +
                "FROM account\n" +
                "WHERE user_id = ?";
        String sqlForUpdatingReceiverBalance = "UPDATE public.account\n" +
                "\tSET balance=?\n" +
                "\tWHERE user_id=?;";
        //does not execute if sender is attempting to send to himself
        if(transferDto.getUserFrom() != transferDto.getUserTo()) {
            try {
                //pulls sender's current balance
                BigDecimal currentBalanceOfReceiver = jdbcTemplate.queryForObject(sqlToGetCurrentBalance, BigDecimal.class, transferDto.getUserTo());
                //checks if the person is trying to send more than they have in their balance
                if(currentBalanceOfReceiver.compareTo(transferDto.getAmount()) >= 0) {
                    BigDecimal newBalanceOfSender = currentBalanceOfReceiver.add(transferDto.getAmount());

                    //Update the sender's balance
                    jdbcTemplate.update(sqlForUpdatingReceiverBalance, newBalanceOfSender, transferDto.getUserTo());
                }
            } catch (DataIntegrityViolationException e) {
                //throw new DaoException("createDepartment() not implemented");
                throw new DaoException("Data integrity violation", e);
            } catch (CannotGetJdbcConnectionException e) {
                throw new DaoException("Invalid Connection", e);
            }
        }
    }

    @Override
    public List<Transfer> retrieveTransfers() {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount  \n" +
                "FROM transfer;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                transfers.add(mapToTransfer(results));
            }
        }  catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return transfers;
    }

    @Override
    public Transfer retrieveTransferByID(Long transferID) {
        Transfer selectTransfer = null;
        String selectSql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount \n" +
                "FROM public.transfer \n" +
                "WHERE transfer_id = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(selectSql, transferID);
            if (results.next()) {
                Transfer transferResults = mapToTransfer(results);
                selectTransfer = mapToTransfer(results);
            }
        }  catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return selectTransfer;
    }

    public User getUserByAccountID(int account_id) {
        User user = null;
        String sql = "SELECT tenmo_user.user_id, tenmo_user.username \n" +
                "FROM account \n" +
                "INNER JOIN tenmo_user ON tenmo_user.user_id = account.user_id \n" +
                "WHERE account.account_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, account_id);
            if (results.next()) {
                user = mapRowToUser(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return user;
    }

    private Transfer mapToTransfer(SqlRowSet rs){
        Transfer newTransfer = new Transfer();
        newTransfer.setTransferId(rs.getLong("transfer_id"));
        newTransfer.setTransferType(rs.getString("transfer_type_id"));
        newTransfer.setTransferStatus(rs.getString("transfer_status_id"));
        System.out.println(rs.getInt("account_from") + " " + rs.getInt("account_to"));
        newTransfer.setUserFrom(getUserByAccountID(rs.getInt("account_from")));
        newTransfer.setUserTo(getUserByAccountID(rs.getInt("account_to")));
        newTransfer.setAmount(rs.getBigDecimal("amount"));

        return newTransfer;

    }

    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));

        return user;
    }


}
