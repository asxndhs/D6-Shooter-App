package org.openjfx.Logic;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;

import static org.openjfx.Logic.Scoring.*;

public class ReadSavedData {

    // TODO: If player edits save file in anyway and voids the encryption, application fails on boot - possible change
    //  this so it ignores saved data and either tells the player to delete the save file or just overwrites it with
    //  new blank saved data and tells the player file is corrupted, has been reset

    // Encrypted scores and stats variables
    static String encryptedStats; // All stat variables encrypted in one string
    static String decryptedStats; // All stat variables decrypted in one string
    static String encryptedHighScores; // High scores encrypted in one string
    static String decryptedHighScores; // High scores decrypted in one string

    static String highScoresString;
    static String statsString;

    private static final String key = "aesEncryptionKey";
    private static final String initVector = "encryptionIntVec";

    public static void createSaveFile() {

        // Create save file, skip if file already exists
        try {
            File myObj = new File("savedData.txt");
            if (myObj.createNewFile()) {
                System.out.println("Save file created: " + myObj.getName());
                writeToFile();
            } else {
                System.out.println("Save file already exists.");
                readLeaderboards(); // Read saved data
                readStatsFromFile(); // Read saved data
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    // Encryption method
    public static String encrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Decryption method
    public static String decrypt(String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static void saveLeaderboards() {

        // Generate and encrypt high scores string
        highScoresString = String.format("%2d %2d %2d %2d %2d %s %s %s %s %s", scoreLeaderboard[0], scoreLeaderboard[1],
                scoreLeaderboard[2], scoreLeaderboard[3], scoreLeaderboard[4], initialsLeaderboard[0], initialsLeaderboard[1],
                initialsLeaderboard[2], initialsLeaderboard[3], initialsLeaderboard[4]);

        // Encrypt high scores string
        encryptedHighScores = encrypt(highScoresString);
        System.out.println("Encrypted high scores - " + encryptedHighScores);

        // Save both score and initials leaderboard to file
        writeToFile();
    }

    public static void readLeaderboards() throws IOException {

        // Read high scores from save file
        encryptedHighScores = Files.readAllLines(Paths.get("savedData.txt")).get(0);
        if (!encryptedHighScores.equals("null")) {
            // Set high score variables
            System.out.println("Does not equal null");
            decryptedHighScores = decrypt(encryptedHighScores);
            System.out.println("Decrypted high scores - " + decryptedHighScores);
        } else {
            return;
        }

        // Assign high scores to decrypted string
        String[] highScores = decryptedHighScores.trim().split("\\s+");
        System.out.println(Arrays.toString(highScores));
        for (int i = 0; i < scoreLeaderboard.length; i++) {
            scoreLeaderboard[i] = Integer.parseInt(highScores[i]);
        }
        for (int i = 0; i < initialsLeaderboard.length; i++) {
            initialsLeaderboard[i] = highScores[i+5];
        }
        System.out.println("High scores - " + Arrays.toString(scoreLeaderboard));
        System.out.println("Initials - " + Arrays.toString(initialsLeaderboard));
    }

    public static void saveStats() {

        // Generate and encrypt stats string
        statsString = String.format("%2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d " +
                "%2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d " +
                "%2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d " +
                "%2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d %2d",
                specialItemsBought, turns, townsVisited, eventsStarted, spacesTravelled, gameTimer, pokerWins, pokerTies,
                pokerLosses, pokerGames, wagerWinnings[0], wagerWinnings[1], wagerWinnings[2], wagerLosses[0], wagerLosses[1],
                wagerLosses[2], bestPokerHand[0], bestPokerHand[1], bestPokerHand[2], bestPokerHand[3], bestPokerHand[4],
                bestPokerHand[5], goldSpent, rationsHandedOut, posseMembersLost, ammoUsed, shootoutsWon, shootoutsLost,
                shootoutsStarted, bestShootoutScore, gamesPlayed, gameWins, winPercentage, gameLosses, daysPlayed,
                specialItemsBoughtHistoric, turnsHistoric, townsVisitedHistoric, eventsStartedHistoric, specialEventsUsedHistoric,
                spacesTravelledHistoric, gameTimerHistoric, pokerWinsHistoric, pokerTiesHistoric, pokerLossesHistoric,
                pokerGamesHistoric, wagerWinningsHistoric[0], wagerWinningsHistoric[1], wagerWinningsHistoric[2],
                wagerLossesHistoric[0], wagerLossesHistoric[1], wagerLossesHistoric[2], bestPokerHandHistoric[0],
                bestPokerHandHistoric[1], bestPokerHandHistoric[2], bestPokerHandHistoric[3], bestPokerHandHistoric[4],
                bestPokerHandHistoric[5], goldSpentHistoric, rationsHandedOutHistoric, posseMembersLostHistoric, ammoUsedHistoric,
                shootoutsWonHistoric, shootoutsLostHistoric, shootoutsStartedHistoric, bestShootoutScoreHistoric, daysPlayedBest,
                finishingPosseBest, finishingGoldBest, finishingFoodBest, finishingAmmoBest, specialItemsBoughtBest,
                turnsBest, spacesTravelledBest, gameTimerBest, pokerWinsBest, pokerTiesBest, pokerLossesBest, wagerWinningsBest[0],
                wagerWinningsBest[1], wagerWinningsBest[2], wagerLossesBest[0], wagerLossesBest[1], wagerLossesBest[2],
                goldSpentBest, rationsHandedOutBest, posseMembersLostBest, ammoUsedBest, shootoutsWonBest,
                shootoutsLostBest, shootoutsStartedBest);

        // Encrypt stats string
        encryptedStats = encrypt(statsString);
        System.out.println("Encrypted stats - " + encryptedStats);

        // Save historic and best stat variables to file
        writeToFile();
    }

    public static void readStatsFromFile() throws IOException {

        // Read stats from the save file
        encryptedStats = Files.readAllLines(Paths.get("savedData.txt")).get(1);
        if (!encryptedStats.equals("null")) {
            decryptedStats = decrypt(encryptedStats);
            System.out.println("Decrypted stats - " + decryptedStats);
        } else {
            return;
        }

        // Assign stats to decrypted string
        String[] stats = decryptedStats.trim().split("\\s+");
        System.out.println(Arrays.toString(stats));
        specialItemsBought = Integer.parseInt(stats[0]);
        turns = Integer.parseInt(stats[1]);
        townsVisited = Integer.parseInt(stats[2]);
        eventsStarted = Integer.parseInt(stats[3]);
        spacesTravelled = Integer.parseInt(stats[4]);
        gameTimer = Integer.parseInt(stats[5]);
        pokerWins = Integer.parseInt(stats[6]);
        pokerTies = Integer.parseInt(stats[7]);
        pokerLosses = Integer.parseInt(stats[8]);
        pokerGames = Integer.parseInt(stats[9]);
        wagerWinnings[0] = Integer.parseInt(stats[10]);
        wagerWinnings[1] = Integer.parseInt(stats[11]);
        wagerWinnings[2] = Integer.parseInt(stats[12]);
        wagerLosses[0] = Integer.parseInt(stats[13]);
        wagerLosses[1] = Integer.parseInt(stats[14]);
        wagerLosses[2] = Integer.parseInt(stats[15]);
        bestPokerHand[0] = Integer.parseInt(stats[16]);
        bestPokerHand[1] = Integer.parseInt(stats[17]);
        bestPokerHand[2] = Integer.parseInt(stats[18]);
        bestPokerHand[3] = Integer.parseInt(stats[19]);
        bestPokerHand[4] = Integer.parseInt(stats[20]);
        bestPokerHand[5] = Integer.parseInt(stats[21]);
        goldSpent = Integer.parseInt(stats[22]);
        rationsHandedOut = Integer.parseInt(stats[23]);
        posseMembersLost = Integer.parseInt(stats[24]);
        ammoUsed = Integer.parseInt(stats[25]);
        shootoutsWon = Integer.parseInt(stats[26]);
        shootoutsLost = Integer.parseInt(stats[27]);
        shootoutsStarted = Integer.parseInt(stats[28]);
        bestShootoutScore = Integer.parseInt(stats[29]);
        gamesPlayed = Integer.parseInt(stats[30]);
        gameWins = Integer.parseInt(stats[31]);
        winPercentage = Integer.parseInt(stats[32]);
        gameLosses = Integer.parseInt(stats[33]);
        daysPlayed = Integer.parseInt(stats[34]);
        specialItemsBoughtHistoric = Integer.parseInt(stats[35]);
        turnsHistoric = Integer.parseInt(stats[36]);
        townsVisitedHistoric = Integer.parseInt(stats[37]);
        eventsStartedHistoric = Integer.parseInt(stats[38]);
        specialEventsUsedHistoric = Integer.parseInt(stats[39]);
        spacesTravelledHistoric = Integer.parseInt(stats[40]);
        gameTimerHistoric = Integer.parseInt(stats[41]);
        pokerWinsHistoric = Integer.parseInt(stats[42]);
        pokerTiesHistoric = Integer.parseInt(stats[43]);
        pokerLossesHistoric = Integer.parseInt(stats[44]);
        pokerGamesHistoric = Integer.parseInt(stats[45]);
        wagerWinningsHistoric[0] = Integer.parseInt(stats[46]);
        wagerWinningsHistoric[1] = Integer.parseInt(stats[47]);
        wagerWinningsHistoric[2] = Integer.parseInt(stats[48]);
        wagerLossesHistoric[0] = Integer.parseInt(stats[49]);
        wagerLossesHistoric[1] = Integer.parseInt(stats[50]);
        wagerLossesHistoric[2] = Integer.parseInt(stats[51]);
        bestPokerHandHistoric[0] = Integer.parseInt(stats[52]);
        bestPokerHandHistoric[1] = Integer.parseInt(stats[53]);
        bestPokerHandHistoric[2] = Integer.parseInt(stats[54]);
        bestPokerHandHistoric[3] = Integer.parseInt(stats[55]);
        bestPokerHandHistoric[4] = Integer.parseInt(stats[56]);
        bestPokerHandHistoric[5] = Integer.parseInt(stats[57]);
        goldSpentHistoric = Integer.parseInt(stats[58]);
        rationsHandedOutHistoric = Integer.parseInt(stats[59]);
        posseMembersLostHistoric = Integer.parseInt(stats[60]);
        ammoUsedHistoric = Integer.parseInt(stats[61]);
        shootoutsWonHistoric = Integer.parseInt(stats[62]);
        shootoutsLostHistoric = Integer.parseInt(stats[63]);
        shootoutsStartedHistoric = Integer.parseInt(stats[64]);
        bestShootoutScoreHistoric = Integer.parseInt(stats[65]);
        daysPlayedBest = Integer.parseInt(stats[66]);
        finishingPosseBest = Integer.parseInt(stats[67]);
        finishingGoldBest = Integer.parseInt(stats[68]);
        finishingFoodBest = Integer.parseInt(stats[69]);
        finishingAmmoBest = Integer.parseInt(stats[70]);
        specialItemsBoughtBest = Integer.parseInt(stats[71]);
        turnsBest = Integer.parseInt(stats[72]);
        spacesTravelledBest = Integer.parseInt(stats[73]);
        gameTimerBest = Integer.parseInt(stats[74]);
        pokerWinsBest = Integer.parseInt(stats[75]);
        pokerTiesBest = Integer.parseInt(stats[76]);
        pokerLossesBest = Integer.parseInt(stats[77]);
        wagerWinningsBest[0] = Integer.parseInt(stats[78]);
        wagerWinningsBest[1] = Integer.parseInt(stats[79]);
        wagerWinningsBest[2] = Integer.parseInt(stats[80]);
        wagerLossesBest[0] = Integer.parseInt(stats[81]);
        wagerLossesBest[1] = Integer.parseInt(stats[82]);
        wagerLossesBest[2] = Integer.parseInt(stats[83]);
        goldSpentBest = Integer.parseInt(stats[84]);
        rationsHandedOutBest = Integer.parseInt(stats[85]);
        posseMembersLostBest = Integer.parseInt(stats[86]);
        ammoUsedBest = Integer.parseInt(stats[87]);
        shootoutsWonBest = Integer.parseInt(stats[88]);
        shootoutsLostBest = Integer.parseInt(stats[89]);
        shootoutsStartedBest = Integer.parseInt(stats[90]);
    }

    public static void writeToFile() {

        // Write saved data to file
        try {
            FileWriter myWriter = new FileWriter("savedData.txt");
            myWriter.write(encryptedHighScores + "\n" + encryptedStats);
            myWriter.close();
            System.out.println("Successfully wrote to save file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
