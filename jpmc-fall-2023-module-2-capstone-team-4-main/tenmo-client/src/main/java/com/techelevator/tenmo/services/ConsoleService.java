package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printApproveRejectMenu() {
        System.out.println();
        System.out.println("1: Approve");
        System.out.println("2: Reject");
        System.out.println("0: Don't approve or reject");
        System.out.println();
    }

    public void printTransferMenu(Transfer[] transfers, User currentUser) {
        printDivider(45);
        System.out.println("Transfers");
        System.out.format("%-10s%-23s%s%n","ID","From/To","Amount");
        printDivider(45);

        for(Transfer t : transfers) {
            String toFromString = buildToFromString(t, currentUser);
            System.out.format("%-10s%-23s%s%n",t.getTransferId(),toFromString,t.getAmount());
        }
        printDivider(10);
        System.out.println();
    }

    private String buildToFromString(Transfer transfer, User currentUser) {
        String toFromString = null;
        if(currentUser.equals(transfer.getUserFrom())) {
            toFromString = "To: "+transfer.getUserTo().getUsername();
        } else {
            toFromString = "From: "+transfer.getUserFrom().getUsername();
        }
        return toFromString;
    }

    public void printPendingTransferMenu(Transfer[] transfers, User currentUser) {
        printDivider(45);
        System.out.println("Pending Transfers");
        System.out.format("%-10s%-23s%s%n","ID","To","Amount");
        printDivider(45);

        for(Transfer t : transfers) {
            if(t.isPending() && t.getUserFrom().equals(currentUser)) {
                System.out.format("%-10s%-23s%s%n", t.getTransferId(),
                        t.getUserTo().getUsername(), t.getAmount());
            }
        }
        printDivider(10);
        System.out.println();
    }

    public void printUserMenu(User[] users) {
        printDivider(20);
        System.out.println("Users");
        System.out.printf("%-10s%s%n","ID","Name");
        printDivider(20);

        for(User u : users) {
            System.out.printf("%-10s%s%n",u.getId(),u.getUsername());
        }
        printDivider(10);
        System.out.println();
    }

    public void printTransferDetails(Transfer transfer) {
        printDivider(20);
        System.out.println("Transfer Details");
        printDivider(20);
        System.out.println("Id: "+transfer.getTransferId());
        System.out.println("From: "+transfer.getUserFrom().getUsername());
        System.out.println("To: "+transfer.getUserTo().getUsername());
        System.out.println("Type: "+transfer.getTransferType());
        System.out.println("Status: "+transfer.getTransferStatus());
        System.out.println("Amount: "+transfer.getAmount());
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
              return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printDivider(int length) {
        for(int i = 0; i < length; i++) {
            System.out.print("-");
        }
        System.out.println();
    }
}
