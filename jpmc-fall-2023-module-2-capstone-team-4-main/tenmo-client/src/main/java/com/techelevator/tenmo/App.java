package com.techelevator.tenmo;

import java.math.BigDecimal;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.UserService;
import com.techelevator.tenmo.services.ConsoleService;

public class App {
    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);
    private final UserService userService = new UserService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
		App app = new App();
    	app.run();
    }

	private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
	}

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser != null) {
            String token = currentUser.getToken();
            accountService.setAuthToken(token);
            transferService.setAuthToken(token);
            userService.setAuthToken(token);
        } else {
           consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();

            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
		}
	}

	private void viewCurrentBalance() {
		BigDecimal balance = accountService.getBalance(currentUser.getUser().getId());
        if (balance != null) {
            System.out.println("Your current account balance is: " + balance);
        } else {
            consoleService.printErrorMessage();
        }
	}

	private void viewTransferHistory() {
        Transfer[] transfers = accountService.retrieveAllTransfers();
        if (transfers != null) {
            consoleService.printTransferMenu(transfers, currentUser.getUser());
            int selectedTransferId = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel): ");
            if(selectedTransferId != 0) {
                Transfer transfer = transferService.retrieveTransferDetails(selectedTransferId);
                if (transfer != null) {
                    consoleService.printTransferDetails(transfer);
                } else {
                    consoleService.printErrorMessage();
                }
            }
        } else {
            consoleService.printErrorMessage();
        }
	}

	private void viewPendingRequests() {
        Transfer[] transfers = accountService.retrieveAllTransfers();
        if (transfers != null) {
            consoleService.printPendingTransferMenu(transfers, currentUser.getUser());
            int selectedTransferId = consoleService.promptForInt("Please enter transfer ID to approve/reject (0 to cancel): ");
            if (selectedTransferId != 0) {
                consoleService.printApproveRejectMenu();
                int menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
                if (menuSelection == 1) {
                    handleApproval(selectedTransferId);
                } else if (menuSelection == 2) {
                    handleRejection(selectedTransferId);
                }
            }
        }
	}

    private void handleApproval(int transferId) {
        Transfer transfer = transferService.updatePendingTransferStatus(transferId, TransferStatus.APPROVED);
        if (transfer != null) {
            System.out.println("Approved transfer of " + transfer.getAmount() + " TE Bucks to " + transfer.getUserTo().getUsername());
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleRejection(int transferId) {
        Transfer transfer = transferService.updatePendingTransferStatus(transferId, TransferStatus.REJECTED);
        if (transfer != null) {
            System.out.println("Rejected transfer of " + transfer.getAmount() + " TE Bucks to " + transfer.getUserTo().getUsername());
        } else {
            consoleService.printErrorMessage();
        }
    }

	private void sendBucks() {
        User[] users = userService.retrieveAllUsers();
        if (users != null) {
            consoleService.printUserMenu(users);
            int toUserId = consoleService.promptForInt("Enter ID of user you are sending to (0 to cancel): ");
            if(toUserId != 0) {
                BigDecimal amount = consoleService.promptForBigDecimal("Enter amount: ");
                long fromUserId = currentUser.getUser().getId();
                TransferDto dto = new TransferDto(fromUserId, toUserId, amount, TransferType.SEND);
                Transfer transfer = transferService.createTransfer(dto);
                if (transfer != null) {
                    System.out.println(amount + " TE Bucks were sent to user " + toUserId);
                } else {
                    consoleService.printErrorMessage();
                }
            }
        } else {
            consoleService.printErrorMessage();
        }
	}

    private void requestBucks() {
        User[] users = userService.retrieveAllUsers();
        if (users != null) {
            consoleService.printUserMenu(users);
            int fromUserId = consoleService.promptForInt("Enter ID of user you are requesting from (0 to cancel): ");
            if(fromUserId != 0) {
                BigDecimal amount = consoleService.promptForBigDecimal("Enter amount: ");
                long toUserId = currentUser.getUser().getId();
                TransferDto dto = new TransferDto(fromUserId, toUserId, amount, TransferType.REQUEST);
                Transfer transfer = transferService.createTransfer(dto);
                if (transfer != null) {
                    System.out.println(amount + " TE Bucks were requested from user " + fromUserId);
                } else {
                    consoleService.printErrorMessage();
                }
            }
        } else {
            consoleService.printErrorMessage();
        }
    }



}
