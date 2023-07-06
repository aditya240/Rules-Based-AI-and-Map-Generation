package s3.ai;



import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import s3.base.S3;
import s3.base.S3Action;
import s3.entities.*;
import s3.util.Pair;

public class AIEmpty implements AI {

	public String m_playerID;
	private List<Rules> rules;

	public void gameEnd() {
	}

	public void gameStarts() {
	}

	public AIEmpty(String playerID) {
		m_playerID = playerID;
		RuleParser ruleParser = new RuleParser();
		ruleParser.loadRules("../S3-CS387/src/s3/ai/rules.txt");
		rules = ruleParser.getRules();
		rules.removeIf(rule -> rule.getConditions().size() < 4);
	}
	
	public String getPlayerId() {
		return m_playerID;
	}

	public void game_cycle(S3 game,WPlayer player,List<S3Action> actions) throws IOException, ClassNotFoundException {
		WUnit u = game.getUnitType(player, WPeasant.class); int first_value = 0, second_value = 0, id = 0, workers_available = 0;
		if(u != null)  id = u.entityID;
		else return;
		if (game.getCycle() % 25 != 0) return;
		for (Rules rule : rules) {
			boolean first = true;
			List<String> conditions = rule.getConditions();
			boolean conditionsMet = true;
			for (String condition : conditions) {
				System.out.println(condition);
				if(condition.startsWith("idle") || condition.startsWith("own") || condition.startsWith("enemy") || condition.startsWith("ownBase") || condition.startsWith("ownBarrack") || condition.startsWith("ownWorker")){
					String cond = condition.substring(0,condition.indexOf('('));
					try {
						boolean result = false;
						if(cond.equalsIgnoreCase("idle")){
							result = this.idle(game, id);
						}
						else if(cond.equalsIgnoreCase("own")){
							result = this.own(game, id);
						}
						else if(cond.equalsIgnoreCase("enemy")){
							result = this.enemy(game, id);
						}
						else if(cond.equalsIgnoreCase("ownBase")){
							result = this.ownBase(game, id);
						}
						else if(cond.equalsIgnoreCase("ownBarrack")){
							result = this.ownBarrack(game, id);
						}
						else{
							result = this.ownWorker(game, id);
						}
						conditionsMet = conditionsMet && result;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else if(condition.startsWith("~idle") || condition.startsWith("~own") || condition.startsWith("~enemy") || condition.startsWith("~ownBase") || condition.startsWith("~ownBarrack") || condition.startsWith("~ownWorker")){
					String cond = condition.substring(1,condition.indexOf('('));
					try {
						boolean result = false;
						if(cond.equalsIgnoreCase("idle")){
							result = this.idle(game, id);
						}
						else if(cond.equalsIgnoreCase("own")){
							result = this.own(game, id);
						}
						else if(cond.equalsIgnoreCase("enemy")){
							result = this.enemy(game, id);
						}
						else if(cond.equalsIgnoreCase("ownBase")){
							result = this.ownBase(game, id);
						}
						else if(cond.equalsIgnoreCase("ownBarrack")){
							result = this.ownBarrack(game, id);
						}
						else{
							result = this.ownWorker(game, id);
						}
						result = !result;
						conditionsMet = conditionsMet && result;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else if (condition.startsWith("workerNeeded")){
					String cond = condition.substring(0,condition.indexOf('('));
					try {
						boolean result = false;
						if(cond.equalsIgnoreCase("workerNeeded")){
							result = this.workerNeeded(game, player);
						}
						conditionsMet = conditionsMet && result;
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				else if(condition.startsWith("workersAvailable")) {
					String cond = condition.substring(0,condition.indexOf('('));
					try {
						int result = this.workersAvailable(game, player);
						workers_available = result;
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				else if(condition.startsWith("goldAvailable") || condition.startsWith("woodAvailable")) {
					String cond = condition.substring(0,condition.indexOf('('));
					try {
						int result = 0;
						if(cond.equalsIgnoreCase("goldAvailable")){
							result = this.goldAvailable(game, player);
						}
						else{
							result = this.woodAvailable(game, player);
						}
						if(first) {
							first_value = result;
							first = false;
						}
						else{
							second_value = result;
							first = true;
							if(first_value > second_value) {
								boolean x = true;
								conditionsMet = conditionsMet && x;
							}
							else{
								conditionsMet = false;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				else if(condition.startsWith("goldNeededFor") || condition.startsWith("woodNeededFor")){
					String cond = condition.substring(0,condition.indexOf('('));
					String unit = condition.substring(condition.indexOf('"') + 1, condition.lastIndexOf('"'));
					try {
						int result = 0;
						if(cond.equalsIgnoreCase("goldNeededFor")){
							if(unit.equalsIgnoreCase("worker")) result =  this.goldNeededFor(game, player, "worker" );
							if(unit.equalsIgnoreCase("base")) result =  this.goldNeededFor(game, player, "base" );
							if(unit.equalsIgnoreCase("barracks")) result =  this.goldNeededFor(game, player, "barracks");
							if(unit.equalsIgnoreCase("knight")) result =  this.goldNeededFor(game, player, "knight" );

						}
						else{
							if(unit.equalsIgnoreCase("worker")) result =  this.woodNeededFor(game, player, "worker" );
							if(unit.equalsIgnoreCase("base")) result =  this.woodNeededFor(game, player, "base" );
							if(unit.equalsIgnoreCase("barracks")) result =  this.woodNeededFor(game, player, "barracks");
							if(unit.equalsIgnoreCase("knight")) result =  this.woodNeededFor(game, player, "knight" );
						}
						second_value = result;
						first = true;
						if(first_value > second_value) {
							boolean x = true;
							conditionsMet = conditionsMet && x;
						}
						else{
							conditionsMet = false;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				else if(condition.startsWith("type")){
					String cond = condition.substring(0,condition.indexOf('('));
					System.out.println(condition);
					String unit = condition.substring(condition.indexOf('"'), condition.lastIndexOf('"'));
					try {
						boolean result = false;
						if (unit.equalsIgnoreCase("worker"))
							result = this.type(game, id, WPeasant.class);
						if (unit.equalsIgnoreCase("base"))
							result = this.type(game, id, WFortress.class);
						if (unit.equalsIgnoreCase("barracks"))
							result = this.type(game, id, WBarracks.class);
						if (unit.equalsIgnoreCase("knight"))
							result = this.type(game, id, WKnight.class);
						conditionsMet = conditionsMet && result;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else if(condition.startsWith("~type")){
					String cond = condition.substring(1,condition.indexOf('('));
					String unit = condition.substring(condition.indexOf('"') + 1, condition.lastIndexOf('"'));
					try {
						boolean result = false;
						if (unit.equalsIgnoreCase("worker"))
							result = this.type(game, id, WPeasant.class);
						if (unit.equalsIgnoreCase("base"))
							result = this.type(game, id, WFortress.class);
						if (unit.equalsIgnoreCase("barracks"))
							result = this.type(game, id, WBarracks.class);
						if (unit.equalsIgnoreCase("knight"))
							result = this.type(game, id, WKnight.class);
						result = !result;
						conditionsMet = conditionsMet && result;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else continue;
			}
			if (conditionsMet) {
				String result = rule.getResult();
				result = result.substring(0,result.indexOf('('));
				try {
					if (result.equalsIgnoreCase("doTrainWorker")) actions.add(this.doTrainWorker(game, player));
					else if (result.equalsIgnoreCase("doTrainKnight")) actions.add(this.doTrainKnight(game, player));
					else if (result.equalsIgnoreCase("idleWorker")) actions.add(this.idleWorker(game, player));
					else if (result.equalsIgnoreCase("doBuildBase")) actions.add(this.doBuildBase(game, player));
					else if (result.equalsIgnoreCase("doBuildBarracks")) actions.add(this.doBuildBarracks(game, player));
					else if (result.equalsIgnoreCase("doHarvestGold")) actions.add(this.doHarvestGold(game, player));
					else if (result.equalsIgnoreCase("doHarvestWood")) actions.add(this.doHarvestWood(game, player));
					else if (result.equalsIgnoreCase("idleKnight")) actions.add(this.idleKnight(game, player));
					else  actions.add(this.doAttack(game, player));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private S3Action doTrainWorker(S3 game, WPlayer player){
		boolean flag = false;
		WBarracks barracks = (WBarracks) game.getUnitType(player, WBarracks.class);
		return (new S3Action(barracks.entityID,S3Action.ACTION_TRAIN, WPeasant.class.getSimpleName()));
	}

	private S3Action doBuildBase(S3 game, WPlayer player){
		WPeasant peasant = (WPeasant) game.getUnitType(player, WPeasant.class);
		Pair<Integer, Integer> loc = game.findFreeSpace(peasant.getX(), peasant.getY(), 3);
		return (new S3Action(peasant.entityID,S3Action.ACTION_BUILD, WFortress.class.getSimpleName(), loc.m_a, loc.m_b));
	}

	private S3Action doBuildBarracks(S3 game, WPlayer player){
		WPeasant peasant = (WPeasant) game.getUnitType(player, WPeasant.class);
		Pair<Integer, Integer> loc = game.findFreeSpace(peasant.getX(), peasant.getY(), 3);
		return (new S3Action(peasant.entityID,S3Action.ACTION_BUILD, WBarracks.class.getSimpleName(), loc.m_a, loc.m_b));
	}

	private S3Action doHarvestGold(S3 game, WPlayer player){
		WPeasant peasant = (WPeasant) game.getUnitType(player, WPeasant.class);
		WUnit mine = game.getUnitType(player, WGoldMine.class);
		return new S3Action(peasant.entityID, S3Action.ACTION_HARVEST, mine.entityID);
	}

	private S3Action doHarvestWood(S3 game, WPlayer player){
		WPeasant peasant = (WPeasant) game.getUnitType(player, WPeasant.class);
		WUnit tree = game.getUnitType(player, WLumberMill.class);
		return new S3Action(peasant.entityID, S3Action.ACTION_HARVEST, tree.entityID);
	}

	private S3Action doTrainKnight(S3 game, WPlayer player){
		boolean flag = false;
		WBarracks barracks = (WBarracks) game.getUnitType(player, WBarracks.class);
		return (new S3Action(barracks.entityID,S3Action.ACTION_TRAIN, WKnight.class.getSimpleName()));
	}

	private S3Action doAttack(S3 game, WPlayer player){
		List<WUnit> footmen = game.getUnitTypes(player, WFootman.class);
		WPlayer enemy = null;
		for (WPlayer entity : game.getPlayers()) {
			if (entity != player) {
				enemy = entity;
				break;
			}
		}
		WUnit enemyTroop = game.getUnitType(enemy, WFootman.class);
		if (null == enemyTroop) {
			enemyTroop = game.getUnitType(enemy, WKnight.class);
		}
		if (null == enemyTroop) {
			enemyTroop = game.getUnitType(enemy, WArcher.class);
		}
		if (null == enemyTroop) {
			enemyTroop = game.getUnitType(enemy, WCatapult.class);
		}
		if (null == enemyTroop) {
			enemyTroop = game.getUnitType(enemy, WPeasant.class);
		}
		if (null == enemyTroop) {
			enemyTroop = game.getUnitType(enemy, WTownhall.class);
		}
		if (null == enemyTroop) {
			enemyTroop = game.getUnitType(enemy, WBarracks.class);
		}
		if (null == enemyTroop) {
			enemyTroop = game.getUnitType(enemy, WLumberMill.class);
		}
		if (null == enemyTroop) {
			enemyTroop = game.getUnitType(enemy, WBlacksmith.class);
		}
		if (null == enemyTroop) {
			enemyTroop = game.getUnitType(enemy, WFortress.class);
		}


		for(WUnit u:footmen) {
			return (new S3Action(u.entityID,S3Action.ACTION_ATTACK, enemyTroop.entityID));
		}

		return new S3Action(footmen.get(0).entityID,S3Action.ACTION_ATTACK, enemyTroop.entityID);
	}

	private S3Action idleWorker(S3 game, WPlayer player){
		WPeasant peasant = (WPeasant) game.getUnitType(player, WPeasant.class);
		WBarracks barracks = (WBarracks) game.getUnitType(player, WBarracks.class);
		return (new S3Action(barracks.entityID,S3Action.ACTION_NONE, WPeasant.class.getSimpleName()));
	}

	private S3Action idleKnight(S3 game, WPlayer player){
		WKnight knight = (WKnight) game.getUnitType(player, WKnight.class);
		WFortress fort = (WFortress) game.getUnitType(player, WFortress.class);
		return (new S3Action(fort.entityID,S3Action.ACTION_NONE, WKnight.class.getSimpleName()));
	}

	private boolean idle(S3 game, int unitId) {
		WUnit unit = game.getUnit(unitId);
		return unit != null && unit.getStatus() == null;
	}

	private boolean own(S3 game, int unitId) {
		WUnit unit = game.getUnit(unitId);
		return unit != null && unit.getOwner().equals(this.getPlayerId());
	}

	private boolean enemy(S3 game, int unitId) {
		WUnit unit = game.getUnit(unitId);
		return unit != null && !unit.getOwner().equals(this.getPlayerId());
	}

	private int workersAvailable(S3 game, WPlayer player) {
		return game.getUnitTypes(player, WPeasant.class).size();
	}

	private int goldAvailable(S3 game, WPlayer player) {
		return player.getGold();
	}

	private int goldNeededFor(S3 game, WPlayer player, String unitType) {
		int x = 0;
		if(unitType.equalsIgnoreCase("worker")) x = new WPeasant().getCost_gold();
		if(unitType.equalsIgnoreCase("base")) x = new WFortress().getCost_gold();
		if(unitType.equalsIgnoreCase("barracks")) x = new WBarracks().getCost_gold();
		if(unitType.equalsIgnoreCase("knight")) x = new WKnight().getCost_gold();
		return x;
	}

	private int woodAvailable(S3 game, WPlayer player) {
		return player.getWood();
	}

	private int woodNeededFor(S3 game, WPlayer player, String unitType) {
		int x = 0;
		if(unitType.equalsIgnoreCase("worker")) x = new WPeasant().getCost_wood();
		if(unitType.equalsIgnoreCase("base")) x = new WFortress().getCost_wood();
		if(unitType.equalsIgnoreCase("barracks")) x = new WBarracks().getCost_wood();
		if(unitType.equalsIgnoreCase("knight")) x = new WKnight().getCost_wood();
		return x;
	}

	private boolean type(S3 game, int unitId, Class<? extends WUnit> unitType) {
		WUnit unit = game.getUnit(unitId);
		return unit != null && unit.getClass().equals(unitType);
	}

	private boolean ownBase(S3 game, int unitId) {
		return type(game, unitId, WFortress.class) && own(game, unitId);
	}

	private boolean ownWorker(S3 game, int unitId) {
		return type(game, unitId, WPeasant.class) && own(game, unitId);
	}

	private boolean ownBarrack(S3 game, int unitId) {
		return type(game, unitId, WBarracks.class) && own(game, unitId);
	}

	private boolean workerNeeded(S3 game, WPlayer player) {
		return workersAvailable(game, player) < 2;
	}

}
