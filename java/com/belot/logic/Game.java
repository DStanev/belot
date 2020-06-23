package com.belot.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.belot.main.Broadcaster;

public class Game {

	public Player player1;
	public Player player2;
	public Player player3;
	public Player player4;

	public Player caller;
	private boolean newgame = false;

	public boolean isAnnouncePhase = true;
	public boolean isGamePhase = false;
	public Player firstToPlay;
	public boolean isActiveAnnounce = false;
	public boolean hasActiveAnnounce = false;
	public String activeAnnounce = "";
	public boolean kapo = false;

	public int gameResultA;
	public int gameResultB;

	public int globalgameResultA;
	public int globalgameResultB;

	public int localgameResultA;
	public int localgameResultB;

	public List<Integer> scoresA = new ArrayList<Integer>();
	public List<Integer> scoresB = new ArrayList<Integer>();

	public int visqt;

	public int lastgameResultA;
	public int lastameResultB;

	public Call call = Call.EMPTY;
	public Card firstCard;

	private List<Card> cards = new ArrayList<Card>();

	private boolean counter = false;
	private boolean reCounter = false;

	private CardFactory cardFactory = new CardFactory();

	private int playerCount = 0;

	private String name;

	private int turnToPlay = 1;

	private int turnToStart = 1;

	private int moves = 0;

	private int turns = 0;

	private int passCount = 0;

	private int turnToCall = 1;

	public Game(String gameName) {
		name = gameName;
	}

	public String getName() {
		return name;
	}

	public boolean makeCall(String player, String calling) {
		player1.activeCard = null;
		player2.activeCard = null;
		player3.activeCard = null;
		player4.activeCard = null;
		turnToCall = incrementTo5(turnToCall);
		Call newCall = Call.getEnum(calling);
		if (newCall != Call.PASS && newCall != Call.COUNTER && newCall != Call.RECOUNTER) {
			call = Call.getEnum(calling);
			caller = getPlayer(player);
		}
		getPlayer(player).call = newCall;
		if (newCall == Call.PASS) {
			passCount++;
		} else {
			passCount = 0;
			if (newCall != Call.COUNTER && newCall != Call.RECOUNTER) {
				counter = false;
				reCounter = false;
			}
			if (newCall == Call.COUNTER) {
				counter = true;
			}
			if (newCall == Call.RECOUNTER) {
				counter = false;
				reCounter = true;
			}
		}
		if (passCount == 3) {
			if (call != Call.EMPTY) {
				startToPlay();
			}
		}
		if (passCount == 4) {
			nextTurnToCall();
		}
		Broadcaster.broadcast("");
		return true;
	}

	public List<String> getPossibleCalls(String player) {
		if (!isAnnouncePhase) {
			return new ArrayList<String>();
		}
		List<Call> possCalls = new LinkedList<Call>(Arrays.asList(Call.class.getEnumConstants()));
		possCalls.remove(Call.EMPTY);
		if (call == Call.CLUB) {
			possCalls.remove(Call.CLUB);
		}
		if (call == Call.DIAMOND) {
			possCalls.remove(Call.CLUB);
			possCalls.remove(Call.DIAMOND);
		}
		if (call == Call.HEART) {
			possCalls.remove(Call.CLUB);
			possCalls.remove(Call.DIAMOND);
			possCalls.remove(Call.HEART);
		}
		if (call == Call.SPADE) {
			possCalls.remove(Call.CLUB);
			possCalls.remove(Call.DIAMOND);
			possCalls.remove(Call.HEART);
			possCalls.remove(Call.SPADE);
		}
		if (call == Call.NO_KOZ) {
			possCalls.remove(Call.CLUB);
			possCalls.remove(Call.DIAMOND);
			possCalls.remove(Call.HEART);
			possCalls.remove(Call.SPADE);
			possCalls.remove(Call.NO_KOZ);
		}
		if (call == Call.All_KOZ) {
			possCalls.remove(Call.CLUB);
			possCalls.remove(Call.DIAMOND);
			possCalls.remove(Call.HEART);
			possCalls.remove(Call.SPADE);
			possCalls.remove(Call.NO_KOZ);
			possCalls.remove(Call.All_KOZ);
		}
		if (counter) {
			possCalls.remove(Call.COUNTER);
			if (call != getpartner(getPlayer(player)).call && call != getPlayer(player).call) {
				possCalls.remove(Call.RECOUNTER);
			}
		}
		if (reCounter) {
			possCalls.remove(Call.COUNTER);
			possCalls.remove(Call.RECOUNTER);
		}
		if (call == Call.EMPTY || call == Call.PASS) {
			possCalls.remove(Call.COUNTER);
			possCalls.remove(Call.RECOUNTER);
		}
		if (!counter) {
			possCalls.remove(Call.RECOUNTER);
		}
		if (call == getpartner(getPlayer(player)).call) {
			possCalls.remove(Call.COUNTER);
			if (!counter) {
				possCalls.remove(Call.RECOUNTER);
			}
		}
		return possCalls.stream().map(x -> x.toString()).collect(Collectors.toList());
	}

	private int incrementTo5(int num) {
		int newNum = num + 1;
		if (newNum == 5) {
			newNum = 1;
		}
		return newNum;
	}

	public boolean makeMove(String playerName, String card, boolean check) {
		if (moves == 0) {
			player1.activeCard = null;
			player2.activeCard = null;
			player3.activeCard = null;
			player4.activeCard = null;
		}
		Card newCard = new Card(card);
		if (check) {
			if (hasAnnounce(getPlayer(playerName), newCard)) {
				isActiveAnnounce = true;
			}
			Broadcaster.broadcast("");
			return true;
		}
		moves++;
		isActiveAnnounce = false;
		Player player = getPlayer(playerName);
		if (moves == 1) {
			firstCard = newCard;
		}
		player.useCard(newCard);
		cards.add(newCard);
		if (moves < 4) {
			turnToPlay = incrementTo5(turnToPlay);
		}
		Broadcaster.broadcast("");
		return true;
	}

	public boolean hasAnnounce(Player player, Card card) {
		return AnnounceManager.getAnnounces(player.cards, call, card, firstCard, turns).size() > 0;
	}

	public Set<String> getAnnounce(String player, String card) {
		return AnnounceManager.getAnnounces(getPlayer(player).cards, call, new Card(card), firstCard, turns);
	}

	public void setAnnounce(Set<String> str, String player) {
		if (!str.isEmpty()) {
			AnnounceManager.setAnnounces(str, getPlayer(player));
			hasActiveAnnounce = true;
			activeAnnounce = generateActiveAnnounce(str, player);
		} else {
			hasActiveAnnounce = false;
		}
	}

	public String generateActiveAnnounce(Set<String> str, String player) {
		StringBuilder stringBuilder = new StringBuilder(player);
		stringBuilder.append(":");
		for (String string : str) {
			stringBuilder.append(" ");
			stringBuilder.append(new Announce(string).getHiddenValue());
		}
		return stringBuilder.toString();
	}

	public boolean isValidMove(String playerName, String card) {
		Card newCard = new Card(card);
		if (moves == 0) {
			return true;
		}
		if (call == Call.NO_KOZ) {
			if (newCard.color == cards.get(0).color) {
				return true;
			} else {
				if (getPlayer(playerName).hasColor(cards.get(0).color)) {
					return false;
				} else {
					return true;
				}
			}
		}

		if (call == Call.All_KOZ) {
			if (newCard.color == cards.get(0).color) {
				if (newCard.beats(getHisghetCard(), call)) {
					return true;
				} else {
					if (getPlayer(playerName).hasHigherCard(call, getHisghetCard())) {
						return false;
					} else {
						return true;
					}
				}
			} else {
				if (getPlayer(playerName).hasColor(cards.get(0).color)) {
					return false;
				} else {
					return true;
				}
			}
		}

		if (newCard.color == cards.get(0).color) {
			if (!newCard.color.toString().equals(call.toString())) {
				return true;
			} else {
				if (newCard.beats(getHisghetCard(), call)) {
					return true;
				} else {
					if (getPlayer(playerName).hasHigherCard(call, getHisghetCard())) {
						return false;
					} else {
						return true;
					}
				}
			}
		} else {
			if (getPlayer(playerName).hasColor(cards.get(0).color)) {
				return false;
			} else {
				if (getHisghetCard() == getpartner(getPlayer(playerName)).activeCard) {
					return true;
				} else {
					if (newCard.color.toString().equals(call.toString())) {
						return true;
					} else {
						if (getPlayer(playerName).hasHigherCard(call, getHisghetCard())) {
							return false;
						} else {
							return true;
						}
					}
				}
			}
		}
	}

	private Card getHisghetCard() {
		Card highestCard = cards.get(0);
		for (int i = 1; i < cards.size(); i++) {
			if (cards.get(i).beats(highestCard, call)) {
				highestCard = cards.get(i);
			}
		}
		return highestCard;
	}

	private Player getWinner() {
		Player next = firstToPlay;
		Player winner = firstToPlay;
		for (int i = 0; i < 3; i++) {
			next = getNextPlayer(next);
			if (next.activeCard.beats(winner.activeCard, call)) {
				winner = next;
			}
		}
		winner.winCards(player1.activeCard);
		winner.winCards(player2.activeCard);
		winner.winCards(player3.activeCard);
		winner.winCards(player4.activeCard);
		return winner;
	}

	private Player getNextPlayer(Player player) {
		if (player.equals(player1)) {
			return player2;
		}
		if (player.equals(player2)) {
			return player3;
		}
		if (player.equals(player3)) {
			return player4;
		}
		return player1;
	}

	public int getPlayerCount() {
		return playerCount;
	}

	public void addPlayer(String playerName) {

		playerCount++;
		if (playerCount == 1) {
			player1 = new Player(playerName);
			player1.cards = cardFactory.generateEightRandomCards().toArray(new Card[8]);
			player1.number = 1;
			cardFactory.sortFirstCards(player1.cards);
		}
		if (playerCount == 2) {
			player2 = new Player(playerName);
			player2.cards = cardFactory.generateEightRandomCards().toArray(new Card[8]);
			player2.number = 2;
			cardFactory.sortFirstCards(player2.cards);
		}
		if (playerCount == 3) {
			player3 = new Player(playerName);
			player3.cards = cardFactory.generateEightRandomCards().toArray(new Card[8]);
			player3.number = 3;
			cardFactory.sortFirstCards(player3.cards);
		}
		if (playerCount == 4) {
			player4 = new Player(playerName);
			player4.cards = cardFactory.generateEightRandomCards().toArray(new Card[8]);
			player4.number = 4;
			cardFactory.sortFirstCards(player4.cards);
			startGame();
		}
	}

	public boolean isMyTurn(String player) {
		if (turnToPlay == 1 && player1 != null) {
			return player.equals(player1.getName());
		}
		if (turnToPlay == 2 && player2 != null) {
			return player.equals(player2.getName());
		}
		if (turnToPlay == 3 && player3 != null) {
			return player.equals(player3.getName());
		}
		if (turnToPlay == 4 && player4 != null) {
			return player.equals(player4.getName());
		}
		return false;
	}

	public boolean isMyTurnToCall(String player) {
		if (turnToCall == 1 && player1 != null) {
			return player.equals(player1.getName());
		}
		if (turnToCall == 2 && player2 != null) {
			return player.equals(player2.getName());
		}
		if (turnToCall == 3 && player3 != null) {
			return player.equals(player3.getName());
		}
		if (turnToCall == 4 && player4 != null) {
			return player.equals(player4.getName());
		}
		return false;
	}

	private void startGame() {
		firstToPlay = player1;
		turnToStart = 1;
		turnToCall = turnToStart;
		turnToPlay = turnToStart;
		cardFactory.reset();
		isAnnouncePhase = true;
		isGamePhase = false;
		call = Call.EMPTY;
		counter = false;
		reCounter = false;
		passCount = 0;
		moves = 0;
		Broadcaster.broadcast("START");
	}

	private void startToPlay() {

		isAnnouncePhase = false;
		isGamePhase = true;
		CardFactory.sortCards(player1, call);
		CardFactory.sortCards(player2, call);
		CardFactory.sortCards(player3, call);
		CardFactory.sortCards(player4, call);
	}

	public void endTurn() {
		if (moves < 4) {
			return;
		}
		firstCard = null;
		Player winner = getWinner();
		cards.clear();
		turnToPlay = winner.number;
		firstToPlay = winner;
		moves = 0;
		turns++;
		if (turns == 8) {
			Broadcaster.broadcast("WIN");
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			winner.lastTen = true;
			calculateResult();
			cardFactory.reset();
			turnToStart = incrementTo5(turnToStart);
			turnToCall = turnToStart;
			turnToPlay = turnToStart;
			firstToPlay = intToPlayer(turnToStart);
			isAnnouncePhase = true;
			isGamePhase = false;
			call = Call.EMPTY;
			counter = false;
			reCounter = false;
			caller = null;
			player1.cards = cardFactory.generateEightRandomCards().toArray(new Card[8]);
			player2.cards = cardFactory.generateEightRandomCards().toArray(new Card[8]);
			player3.cards = cardFactory.generateEightRandomCards().toArray(new Card[8]);
			player4.cards = cardFactory.generateEightRandomCards().toArray(new Card[8]);
			cardFactory.sortFirstCards(player1.cards);
			cardFactory.sortFirstCards(player2.cards);
			cardFactory.sortFirstCards(player3.cards);
			cardFactory.sortFirstCards(player4.cards);
			player1.wincards.clear();
			player2.wincards.clear();
			player3.wincards.clear();
			player4.wincards.clear();
			player1.cleadAnnounces();
			player2.cleadAnnounces();
			player3.cleadAnnounces();
			player4.cleadAnnounces();
			passCount = 0;
			moves = 0;
			player1.call = Call.EMPTY;
			player2.call = Call.EMPTY;
			player3.call = Call.EMPTY;
			player4.call = Call.EMPTY;
			winner.lastTen = false;
			turns = 0;
			checkForEnd();
		}
		Broadcaster.broadcast("");
	}

	private void checkForEnd() {
		if (localgameResultA > 151 || localgameResultB > 151) {
			if (!kapo) {
				if (localgameResultA > localgameResultB) {
					globalgameResultA++;
					turnToStart = 1;
					turnToCall = turnToStart;
					turnToPlay = turnToStart;
					firstToPlay = player1;
					localgameResultA = 0;
					localgameResultB = 0;
					newgame = true;
				}
				if (localgameResultA < localgameResultB) {
					globalgameResultB++;
					turnToStart = 2;
					turnToCall = turnToStart;
					turnToPlay = turnToStart;
					firstToPlay = player2;
					localgameResultB = 0;
					localgameResultA = 0;
					newgame = true;
				}
				kapo = false;
			}
		}
	}

	private Player intToPlayer(int num) {
		if (num == 1) {
			return player1;
		}
		if (num == 2) {
			return player2;
		}
		if (num == 3) {
			return player3;
		}
		return player4;
	}

	private int resultA = 0;
	private int resultB = 0;

	private void calculateResult() {
		int prevA = localgameResultA;
		int prevB = localgameResultB;
		Set<Announce> teamA = new HashSet<Announce>();
		Set<Announce> teamB = new HashSet<Announce>();

		teamA.addAll(player1.announces);
		teamA.addAll(player3.announces);
		teamB.addAll(player2.announces);
		teamB.addAll(player4.announces);

		if (AnnounceManager.whiTheFuck(teamA, teamB, 1) == 1) {
			addAnnounce(1, teamA, 1);
		}
		if (AnnounceManager.whiTheFuck(teamA, teamB, 1) == 2) {
			addAnnounce(1, teamB, 2);
		}
		if (AnnounceManager.whiTheFuck(teamA, teamB, 2) == 1) {
			addAnnounce(2, teamA, 1);
		}
		if (AnnounceManager.whiTheFuck(teamA, teamB, 2) == 2) {
			addAnnounce(2, teamB, 2);
		}

		addBelots();
		addCards();
		addLastTen();

		if (!counter && !reCounter) {
			if (resultA > resultB && (caller == player1 || caller == player3)) {
				localgameResultA += zakragliNadolu(resultA);
				localgameResultA += visqt;
				localgameResultB += zakragliNagore(resultB);
				visqt = 0;
			}
			if (resultA > resultB && (caller == player2 || caller == player4)) {
				localgameResultA += zakragliNadolu(resultA + resultB);
				localgameResultA += visqt;
				visqt = 0;
			}
			if (resultA < resultB && (caller == player1 || caller == player3)) {
				localgameResultB += zakragliNagore(resultB + resultA);
				localgameResultB += visqt;
				visqt = 0;
			}
			if (resultA < resultB && (caller == player2 || caller == player4)) {
				localgameResultB += zakragliNadolu(resultB);
				localgameResultA += zakragliNagore(resultA);
				localgameResultB += visqt;
				visqt = 0;
			}
			if (resultA == resultB) {
				if (caller == player1 || caller == player3) {
					localgameResultB += zakragliNagore(resultB);
					visqt = zakragliNagore(resultA);
				} else {
					localgameResultA += zakragliNagore(resultA);
					visqt = zakragliNagore(resultB);
				}
			}
		}
		if (counter) {
			if (resultA > resultB) {
				localgameResultA += (2 * zakragliNadolu(resultA + resultB));
				localgameResultA += visqt;
				visqt = 0;
			}
			if (resultA < resultB) {
				localgameResultB += (2 * zakragliNadolu(resultA + resultB));
				localgameResultB += visqt;
				visqt = 0;
			}
			if (resultA == resultB) {
				if (caller == player1 || caller == player3) {
					localgameResultB += zakragliNadolu(resultA + resultB);
					visqt = zakragliNadolu(resultA + resultB);
				} else {
					localgameResultA += zakragliNadolu(resultA + resultB);
					visqt = zakragliNadolu(resultA + resultB);
				}
			}
		}
		if (reCounter) {
			if (resultA > resultB) {
				localgameResultA += (4 * zakragliNadolu(resultA + resultB));
				localgameResultA += visqt;
				visqt = 0;
			}
			if (resultA < resultB) {
				localgameResultB += (4 * zakragliNadolu(resultA + resultB));
				localgameResultB += visqt;
				visqt = 0;
			}
			if (resultA == resultB) {
				if (caller == player1 || caller == player3) {
					localgameResultB += 2 * zakragliNadolu(resultA + resultB);
					visqt = 2 * zakragliNadolu(resultA + resultB);
				} else {
					localgameResultA += 2 * zakragliNadolu(resultA + resultB);
					visqt = 2 * zakragliNadolu(resultA + resultB);
				}
			}
		}

		if (newgame) {
			scoresA.clear();
			scoresB.clear();
			newgame = false;
		}
		scoresA.add(new Integer(localgameResultA));
		scoresB.add(new Integer(localgameResultB));
		resultA = 0;
		resultB = 0;
	}

	private void addLastTen() {
		if (player1.lastTen || player3.lastTen) {
			resultA += 10;
		} else {
			resultB += 10;
		}
	}

	private void addAnnounce(int type, Set<Announce> announces, int team) {
		for (Announce announce : announces) {
			if (announce.type == type) {
				if (team == 1) {
					resultA += announce.value;
				} else {
					resultB += announce.value;
				}
			}
		}
	}

	private boolean iskapoA() {
		return (player1.wincards.isEmpty() && player3.wincards.isEmpty());

	}

	private boolean iskapoB() {
		return (player2.wincards.isEmpty() && player4.wincards.isEmpty());

	}

	public int zakragliNagore(int res) {
		int result = res;
		if (call == Call.All_KOZ) {
			int base = result / 10;
			if (result % 10 >= 4) {
				return base + 1;
			}
			return base;
		}
		if (call == Call.NO_KOZ) {
			result *= 2;
			int base = result / 10;
			if (result % 10 >= 5) {
				return base + 1;
			}
			return base;
		}
		int base = result / 10;
		if (result % 10 >= 6) {
			return base + 1;
		}
		return base;
	}

	public int zakragliNadolu(int res) {
		int result = res;
		if (call == Call.All_KOZ) {
			int base = result / 10;
			if (result % 10 >= 5) {
				return base + 1;
			}
			return base;
		}
		if (call == Call.NO_KOZ) {
			result *= 2;
			int base = result / 10;
			if (result % 10 >= 6) {
				return base + 1;
			}
			return base;
		}
		int base = result / 10;
		if (result % 10 >= 7) {
			return base + 1;
		}
		return base;
	}

	private void addCards() {
		addCards(player1);
		addCards(player3);
		addCards(player2);
		addCards(player4);
		if (iskapoA()) {
			resultB += 90;
			kapo = true;
		} else {
			if (iskapoB()) {
				resultA += 90;
				kapo = true;
			} else {
				kapo = false;
			}
		}
	}

	private void addCards(Player player) {
		for (Card card : player.wincards) {
			int cardValue;
			if (call == Call.NO_KOZ) {
				cardValue = card.bezValue;
			} else {
				if (call == Call.All_KOZ) {
					cardValue = card.kozValue;
				} else {
					if (call.toString().equals(card.color.toString())) {
						cardValue = card.kozValue;
					} else {
						cardValue = card.bezValue;
					}
				}
			}
			if (player == player1 || player == player3) {
				resultA += cardValue;
			} else {
				resultB += cardValue;
			}
		}
	}

	private void addBelots() {
		resultA += player1.belots * 20;
		resultA += player3.belots * 20;
		resultB += player2.belots * 20;
		resultB += player4.belots * 20;
	}

	private void nextTurnToCall() {
		cardFactory.reset();
		player1.cards = cardFactory.generateEightRandomCards().toArray(new Card[8]);
		player2.cards = cardFactory.generateEightRandomCards().toArray(new Card[8]);
		player3.cards = cardFactory.generateEightRandomCards().toArray(new Card[8]);
		player4.cards = cardFactory.generateEightRandomCards().toArray(new Card[8]);
		cardFactory.sortFirstCards(player1.cards);
		cardFactory.sortFirstCards(player2.cards);
		cardFactory.sortFirstCards(player3.cards);
		cardFactory.sortFirstCards(player4.cards);
		turnToStart = incrementTo5(turnToStart);
		turnToCall = turnToStart;
		turnToPlay = turnToStart;
		firstToPlay = getNextPlayer(firstToPlay);
		call = Call.EMPTY;
		counter = false;
		reCounter = false;
		passCount = 0;
		moves = 0;
		player1.call = Call.EMPTY;
		player2.call = Call.EMPTY;
		player3.call = Call.EMPTY;
		player4.call = Call.EMPTY;
	}

	public Set<String> getPlayerNames() {
		Set<String> result = new HashSet<String>();
		if (player1 != null) {
			result.add(player1.getName());
		}
		if (player2 != null) {
			result.add(player2.getName());
		}
		if (player3 != null) {
			result.add(player3.getName());
		}
		if (player4 != null) {
			result.add(player4.getName());
		}
		return result;
	}

	public Player getPlayer(String playername, String position) {
		Player my = getPlayer(playername);
		if (my.equals(player1)) {
			if (position.equals("partner")) {
				return player3;
			}
			if (position.equals("left")) {
				return player4;
			}
			if (position.equals("right")) {
				return player2;
			}
		}
		if (my.equals(player2)) {
			if (position.equals("partner")) {
				return player4;
			}
			if (position.equals("left")) {
				return player1;
			}
			if (position.equals("right")) {
				return player3;
			}
		}
		if (my.equals(player3)) {
			if (position.equals("partner")) {
				return player1;
			}
			if (position.equals("left")) {
				return player2;
			}
			if (position.equals("right")) {
				return player4;
			}
		}
		if (my.equals(player4)) {
			if (position.equals("partner")) {
				return player2;
			}
			if (position.equals("left")) {
				return player3;
			}
			if (position.equals("right")) {
				return player1;
			}
		}
		return my;
	}

	public Player getPlayer(String playerName) {
		if (player1 != null && player1.getName().equals(playerName)) {
			return player1;
		}
		if (player2 != null && player2.getName().equals(playerName)) {
			return player2;
		}
		if (player3 != null && player3.getName().equals(playerName)) {
			return player3;
		}
		if (player4 != null && player4.getName().equals(playerName)) {
			return player4;
		}
		return null;
	}

	private Player getpartner(Player player) {
		if (player.equals(player1)) {
			return player3;
		}
		if (player.equals(player2)) {
			return player4;
		}
		if (player.equals(player3)) {
			return player1;
		}
		return player2;
	}

	public boolean isActiveToCall(String player, String pos) {
		return turnToCall == getPlayer(player, pos).number;
	}

	public boolean isActiveToplay(String player, String pos) {
		return turnToPlay == getPlayer(player, pos).number;
	}

	public String isCounter() {
		if (counter) {
			return "Контра";
		}
		if (reCounter) {
			return "Реконтра";
		}
		return "";
	}

	public List<DummyResult> getLocalgameResult(String plString) {
		List<DummyResult> res = new ArrayList<DummyResult>();
		if (getPlayer(plString).equals(player1) || getPlayer(plString).equals(player3)) {
			for (int i = 0; i < scoresA.size(); i++) {
				res.add(new DummyResult(scoresA.get(i), scoresB.get(i)));
			}
		} else {
			for (int i = 0; i < scoresA.size(); i++) {
				res.add(new DummyResult(scoresB.get(i), scoresA.get(i)));
			}
		}
		return res;
	}

	public int getGloaballgameResultA(String plString) {
		if (getPlayer(plString).equals(player1) || getPlayer(plString).equals(player3)) {
			return globalgameResultA;
		} else {
			return globalgameResultB;
		}
	}

	public int getGloaballgameResultB(String plString) {
		if (getPlayer(plString).equals(player1) || getPlayer(plString).equals(player3)) {
			return globalgameResultB;
		} else {
			return globalgameResultA;
		}
	}
}
