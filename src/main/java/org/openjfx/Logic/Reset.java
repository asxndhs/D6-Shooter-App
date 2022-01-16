package org.openjfx.Logic;

import org.openjfx.SettingsScreens.Settings;

import java.io.IOException;
import java.util.Arrays;

import static org.openjfx.GameScreens.ChooseOption.*;
import static org.openjfx.GameScreens.Events.*;
import static org.openjfx.GameScreens.Game.*;
import static org.openjfx.GameScreens.PickRoute.*;
import static org.openjfx.GameScreens.Poker.*;
import static org.openjfx.GameScreens.PosseLoss.*;
import static org.openjfx.GameScreens.Rations.*;
import static org.openjfx.GameScreens.ResolveDice.*;
import static org.openjfx.GameScreens.Shootout.*;
import static org.openjfx.GameScreens.SpecialRoll.*;
import static org.openjfx.GameScreens.Town.*;
import static org.openjfx.GameScreens.WagonTrain.*;
import static org.openjfx.Logic.Map.*;
import static org.openjfx.Logic.Day.*;
import static org.openjfx.Logic.Inventory.*;
import static org.openjfx.Logic.ReadSavedData.saveLeaderboards;
import static org.openjfx.Logic.ReadSavedData.saveStats;
import static org.openjfx.Logic.RollMethods.Roll.*;
import static org.openjfx.Logic.RollMethods.RollActions.*;
import static org.openjfx.Logic.GameOverMethods.*;
import static org.openjfx.Logic.TownMethods.BuyItems.*;
import static org.openjfx.Logic.TownMethods.SpecialItems.*;
import static org.openjfx.Logic.TownMethods.FindTown.*;
import static org.openjfx.Logic.EventMethods.FullEvents.*;
import static org.openjfx.Logic.EventMethods.EventCall.*;
import static org.openjfx.Logic.RationsMethods.*;
import static org.openjfx.Logic.Scoring.*;
import static org.openjfx.Logic.ShootoutMethods.*;
import static org.openjfx.SettingsScreens.Settings.*;

public class Reset {

    public static void endOfTurn() throws IOException {
        // End of turn reset variables
        eventSpace = false;
        pickRouteSpace = false;
        townSpace = false;
        movement = 0;
        extraMovement = 0;

        // Dice variables
        rollCounter = 0;
        ones = 0;
        twos = 0;
        threes = 0;
        fours = 0;
        fives = 0;
        sixes = 0;
        hide = 0;
        seekShelter = 0;
        backroads = 0;
        fight = 0;
        sixesChange = 0;
        remainingHideDice = 0;
        resetDiceVariables(); // Not technically needed

        // Resolve dice variables
        onesChecked = false;
        twosChecked = false;
        threesChecked = false;
        hideChecked = false;
        seekShelterChecked = false;
        backroadsChecked = false;
        fightChecked = false;
        fivesChecked = false;
        fiveSpecialRoll = false;
        sixesChecked = false;
        sixSpecialRoll = false;
        playShootout = false;
        stage = 1; // Shootout scene variable

        negatePosseLoss = false;
        stopPosseLoss = false;
        posseItemsDisabled = false;

        goldHillsUsed = false;
        lostFamilyUsed = false;
        indianGuideUsed = false;
        rollStarted = false;

        startOfTurn();
    }

    public static void resetGameVariables() {

        // Scene progress variables
        chooseOptionSceneProgress = 0;
        eventsSceneProgress = 0;
        gameSceneProgress = 0;
        pickRouteSceneProgress = 0;
        pokerSceneProgress = 0;
        posseLossSceneProgress = 0;
        rationsSceneProgress = 0;
        resolveDiceSceneProgress = 0;
        shootoutSceneProgress = 0;
        specialRollSceneProgress = 0;
        townSceneProgress = 0;
        wagonTrainSceneProgress = 0;

        // Reset variables before the start of a new game
        day = 1;
        setDayLabel("Day: " + day);
        timeLimit = dayLimit;
        playerSpace = 1;
        setPlayerSpaceLabel("Player space: " + playerSpace);
        posse = Settings.startingPosse;
        food = startingFood;
        ammo = startingAmmo;
        gold = startingGold;
        finish = townSpaces[finishingTownIndex];
        movement = 0;
        extraMovement = 0;

        // Set opening game statuses
        setDaySpaceStatus("It is Day " + day + " and you are on space " + playerSpace);
        setInventoryStatus("You have " + posse + " Posse members and an inventory of " + gold + " Gold, " + food + " Food and " + ammo + " Ammo");

        // Dice variables
        rollCounter = 0;
        ones = 0;
        twos = 0;
        threes = 0;
        fours = 0;
        fives = 0;
        sixes = 0;
        hide = 0;
        seekShelter = 0;
        backroads = 0;
        fight = 0;
        sixesChange = 0;
        remainingHideDice = 0;
        onesChecked = false;
        twosChecked = false;
        threesChecked = false;
        hideChecked = false;
        seekShelterChecked = false;
        backroadsChecked = false;
        fightChecked = false;
        fivesChecked = false;
        sixesChecked = false;
        playShootout = false;
        stage = 1; // Shootout scene variable

        // Reset special item booleans/variables
        compassInventory = startingSpecialItems.contains(1);
        hunterInventory = startingSpecialItems.contains(2);
        prospectorsMapInventory = startingSpecialItems.contains(3);
        binocularsInventory = startingSpecialItems.contains(4);
        medicineBandagesInventory = startingSpecialItems.contains(5);
        if (startingSpecialItems.contains(6)) {
            betterGunsInventory = true;
            betterGunsModifier = startingBetterGuns;
        } else {
            betterGunsInventory = false;
            betterGunsModifier = 0;
        }
        compassUsed = 0;
        hunterUsed = 0;
        hunterLost = false;
        prospectorsMapUsed = false;
        binocularsCounter = false;
        negatePosseLoss = false;

        specialEvent = false;
        specialEventUsed = false;
        eventSpace = false;
        stopPosseLoss = false;
        posseItemsDisabled = false;

        // Event Booleans
        eventTypeFull = eventTypeStatus;
        goldHillsEvent = false; // Boolean for There's Gold in them Hills Event
        downhillEvent = false; // Boolean for Downhill Ride Event
        lostGunsightEvent = false; // Boolean for Lost Gunsight Mine Event
        crittersEvent = false; // Boolean for Critters Everywhere Event
        prisonerEvent = false; // Boolean for Prisoner Event
        prisonerRed6 = false; // Boolean for Prisoner Red 6
        prisonerRedDie1Lock = false; // Boolean for Prisoner lock Red die 1
        prisonerRedDie2Lock = false; // Boolean for Prisoner lock Red die 2
        prisonerRedDie3Lock = false; // Boolean for Prisoner lock Red die 3
        campfireEvent = false; // Boolean for Campfire Songs Event
        familyEvent = false; // Boolean for Lost Family Event
        desertersEvent = false; // Boolean for Army Deserters Event
        deserterRepeatCheck = false;
        deserter1 = false;
        deserter2 = false;
        indianEvent = false; // Boolean for Indian Guide Event
        eventDie1Lock = false; // Lock boolean for event dice
        eventDie2Lock = false; // Lock boolean for event dice
        banditsShootout = false; // Boolean for bandits event
        prisonerShootoutLoss = false; // Boolean for prisoner event tracking shootout losses

        // Town variables
        townSpace = false;
        pickRouteSpace = false;
        specialItemsAllPurchased = false;

        // Reset game over variables
        win = false;
        loss = false;
        rationsDay = false;
        rationsGameOver = false;
        highScore = false;

        // Shootout reset
        shootoutWin = false;
        shootoutLoss = false;
        replayShootout = false;
        stage = 1;

        // Stats reset
        specialItemsBought = 0;
        turns = 0;
        townsVisited = 0;
        eventsStarted = 0;
        spacesTravelled = 0;
        gameTimer = 0;
        pokerWins = 0;
        pokerTies = 0;
        pokerLosses = 0;
        pokerGames = 0;
        Arrays.fill(wagerWinnings,0);
        Arrays.fill(wagerLosses,0);
        Arrays.fill(bestPokerHand,0);
        goldSpent = 0;
        rationsHandedOut = 0;
        posseMembersLost = 0;
        ammoUsed = 0;
        shootoutsWon = 0;
        shootoutsLost = 0;
        shootoutsStarted = 0;
        bestShootoutScore = 0;
    }

    public static void resetScoresStats() {

        // Reset high scores
        Arrays.fill(scoreLeaderboard, 0);
        Arrays.fill(initialsLeaderboard,"---");

        // Reset all-time stats
        gamesPlayed = 0;
        gameWins = 0;
        winPercentage = 0;
        gameLosses = 0;
        daysPlayed = 0;
        specialItemsBoughtHistoric = 0;
        turnsHistoric = 0;
        townsVisitedHistoric = 0;
        eventsStartedHistoric = 0;
        specialEventsUsedHistoric = 0;
        spacesTravelledHistoric = 0;
        gameTimerHistoric = 0;
        pokerWinsHistoric = 0;
        pokerTiesHistoric = 0;
        pokerLossesHistoric = 0;
        pokerGamesHistoric = 0;
        Arrays.fill(wagerWinningsHistoric, 0);
        Arrays.fill(wagerLossesHistoric,0);
        Arrays.fill(bestPokerHandHistoric,0);
        goldSpentHistoric = 0;
        rationsHandedOutHistoric = 0;
        posseMembersLostHistoric = 0;
        ammoUsedHistoric = 0;
        shootoutsWonHistoric = 0;
        shootoutsLostHistoric = 0;
        shootoutsStartedHistoric = 0;
        bestShootoutScoreHistoric = 0;

        // Reset best stats
        daysPlayedBest = 0;
        finishingPosseBest = 0;
        finishingGoldBest = 0;
        finishingFoodBest = 0;
        finishingAmmoBest = 0;
        specialItemsBoughtBest = 0;
        turnsBest = 0;
        spacesTravelledBest = 0;
        gameTimerBest = 0;
        pokerWinsBest = 0;
        pokerTiesBest = 0;
        pokerLossesBest = 0;
        Arrays.fill(wagerWinningsBest,0);
        Arrays.fill(wagerLossesBest,0);
        goldSpentBest = 0;
        rationsHandedOutBest = 0;
        posseMembersLostBest = 0;
        ammoUsedBest = 0;
        shootoutsWonBest = 0;
        shootoutsLostBest = 0;
        shootoutsStartedBest = 0;

        saveLeaderboards();
        saveStats();
    }
}
