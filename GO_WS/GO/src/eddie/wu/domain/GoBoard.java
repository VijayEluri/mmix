/*
 * Created on 2005-4-21
 * 现在有3000行代码.其实都是围绕有限的数据结构展开的,只是实现的功能不同.为了<br/>
 * 将代码尽量分散开。决定将前进后退的功能和根据局面初始化的功能放在单独的类中<br/>
 * 死活判断以及其他的形式分析也是这样处理。所形成的类层次结构不是为了代码复用<br/>
 * 而是为了让一个类的代码不要过于膨胀<br/>
 */
package eddie.wu.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.api.GoBoardInterface;
import eddie.wu.domain.analy.EyeResult;
import eddie.wu.domain.analy.FinalResult;
import eddie.wu.manual.SimpleGoManual;
import eddie.wu.manual.TreeGoManual;
import eddie.wu.search.global.Candidate;
import eddie.wu.search.global.MakeEyeComparator;

/**
 * this program is refactored from the old program which use array as data
 * structure and did not use any Collection framework. <br/>
 * 围棋程序的核心类.完成数气提子.
 * 
 * @author eddie
 */
public class GoBoard extends GoBoardBackward implements Cloneable,
		Serializable, GoBoardInterface {
	private transient static final Logger log = Logger.getLogger(GoBoard.class);

	public GoBoard(int boardSize) {
		super(boardSize);

	}

	/**
	 * 
	 * really used Constructor.
	 * 
	 */
	public GoBoard() {
		this(Constant.BOARD_SIZE);
	}

	/**
	 * no double check really used Constructor.
	 * 
	 */
	// public GoBoard(BoardColorState boardState, int numberOfSteps) {
	// super(boardState,numberOfSteps);
	// }

	/**
	 * no double check really used Constructor. ，<br/>
	 * 适用于做题, 局面已经初始化好了.或者让子棋的局面
	 */
	public GoBoard(BoardColorState boardState) {
		super(boardState);

	}

	/**
	 * 适用于做题, 局面已经初始化好了.或者让子棋的局面
	 * 
	 * @param board
	 */
	public GoBoard(byte[][] board) {

		super(BoardColorState.getInstance(board, Constant.BLACK));

	}

	public GoBoard(TreeGoManual goManual) {
		super(goManual.getInitState());
		this.initTree(goManual);
	}

	public GoBoard(byte[][] board, int whoseTurn) {

		super(BoardColorState.getInstance(board, whoseTurn));
	}

	public List<Candidate> getCandidate_forAttacker(Set<Point> targets,
			Point target, Set<Point> scope, Set<Point> externalBreath,
			int color, boolean forTarget, boolean loopSuperior) {
		List<Candidate> candidates = this.getCandidate_forTarget(targets,
				target, scope, color, forTarget, loopSuperior);
		List<Candidate> gifts = new ArrayList<Candidate>();
		for (Iterator<Candidate> iter = candidates.iterator(); iter.hasNext();) {
			Candidate candidate = iter.next();// candidates
			if (candidate.isGifting()) {
				gifts.add(candidate);
				iter.remove();
			}
		}

		boolean closeExtBreath = false;
		if (candidates.isEmpty()) {
			closeExtBreath = true;
		}
		// else if (candidates.get(0).getStep().isGiveUp()) {
		// closeExtBreath = false;
		// }
		Candidate candidateOne = null;
		if (closeExtBreath) {
			// externalBreath maybe dynamically changed.
			externalBreath.addAll(this.getBlock(target).getBreathPoints());
			for (Point point : externalBreath) {

				if (this.getColor(point) != Constant.BLANK) {
					// avoid wrong candidate!.
					continue;
				} else {
					/**
					 * the breath are NOT equivalent!<br/>
					 * need to consider the sequence.<br/>
					 * simplified version only avoid suicide and gifting.
					 */
					Set<Point> breath = this.breathAfterPlay(point, color);
					if (breath.size() < 1)
						continue;

					if (breath.size() >= 1) {
						Candidate candidateP = new Candidate();
						candidateP.setStep(new Step(point, color,
								this.shoushu + 1));

						if (breath.size() == 1) {
							candidateOne = candidateP;
							continue;
						} else {
							candidates.add(candidateP);
							break;
						}
					}
				}
			}
			if (candidateOne != null && candidates.contains(candidateOne)) {
				candidates.add(candidateOne);
			}

			if (log.isEnabledFor(Level.WARN))
				log.warn("破眼方在眼内没有棋可走时，考虑破眼方紧外气");
		}
		// put the gifting candidate at the end of list.
		candidates.addAll(gifts);
		if (candidates.isEmpty()) {
			// add one pass step anyway!
			Candidate candidateP = new Candidate();
			candidateP.setStep(new Step(null, color, getShoushu() + 1));
			candidates.add(candidateP);
		}
		return candidates;

	}

	/**
	 * 大眼死 活搜索中选取候选点。<br/>
	 * 成眼和破眼方的排序标准可能不同。同时第一步和第三步的排序标准也可能不同 <br/>
	 * 排序可以避免一些无意义的局面或选择，但是我们仍然需要能够处理这些变化，。<br/>
	 * 成眼方如果选择了缩小眼位的废棋，如何继续。缩小了，用归纳法。不用继续计算。 直二是死棋。做不出两眼。<br/>
	 * 计算何时该停止，做静态判断？<br/>
	 * 这里大眼死活的上下文是外围是对方活棋，因此只考虑在目前的眼位中做出两眼或者和眼位内的敌子共活。 <br/>
	 * also called by _forAttacker(). so forTarget parameter is still necessary.
	 * 
	 * @param scope
	 *            候选点的范围就是原始眼位所在眼块的所有点
	 * @param color
	 * @return
	 */
	public List<Candidate> getCandidate_forTarget(Set<Point> targets,
			Point target, Set<Point> scope, int color, boolean forTarget,
			boolean loopSuperior) {
		List<Point> can = new ArrayList<Point>();
		List<Candidate> cans = new ArrayList<Candidate>();

		/**
		 * we need to extend the scope in case of one block of target group is
		 * eaten and we can eat back.--hot fix
		 * 
		 */
		if (this.noStep() == false && this.getLastStep().isPass() == false) {
			if (this.getBreaths(this.getLastPoint()) == 1) {
				scope.add(this.getBlock(this.getLastPoint()).getLastBreath());
			}
			if (this.getLastStep().getNeighborState().isEating()) {
				for (Block eaten : (getLastStep().getNeighborState()
						.getEatenBlocks())) {
					scope.addAll(eaten.allPoints);
				}
			}
		}
		// Point prohibitedP = getLastStep().getProhibittedPoint();
		// if (prohibitedP != null) {
		//
		// // }
		boolean loop;
		for (Point point : scope) {
			// because validate below has flaw without continue, we need check
			// here to ensure correctness.
			if (this.getColor(point) != Constant.BLANK) {
				continue;
			}
			/**
			 * 打劫的禁着点进行特殊处理：<br/>
			 * 劫材有利的话，通过大劫材（绝对劫材）提回劫。 劫材不利的话不考虑这个选择。
			 * 
			 */
			loop = false;
			if (this.validate(point, color) == false) {
				if (noStep())
					continue;
				Point prohibitedP = getLastStep().getProhibittedPoint();
				if (prohibitedP != null && prohibitedP == point) {
					if (loopSuperior) {
						loop = true;
						if (log.isEnabledFor(Level.WARN))
							log.warn("color=" + color + "劫材有利" + " 考虑 " + point
									+ "强行提回劫。");
					} else {
						if (log.isEnabledFor(Level.WARN))
							log.warn("color=" + color + "劫材不利，弃权，" + " 不考虑"
									+ point);
						// 找劫材，局部就是弃权。
						if (forTarget == false) { // avoid duplicate give up.
							Candidate candidate = new Candidate();
							candidate.setStep(new Step(null, color,
									this.shoushu + 1));
							cans.add(candidate);
						}// target give up is done later.
						continue;
					}

				} else {

					continue;
				}
			}
			SimpleNeighborState state = this.getNeighborState_bigEye(point,
					color);
			Candidate candidate = new Candidate();
			candidate.setStep(new Step(point, color, this.shoushu + 1));
			candidate.setEating(state.isEating());
			if (state.isEating()) {
				boolean justified = false;

				// 气数为一,又不能长气,必然可以被提子.
				for (Block block : state.getEatenBlocks()) {
					if (block.getMinBreathEnemyBlock().getBreaths() == 1) {
						justified = true;
						break;
					}
				}
				if (justified == false) {
					// state.setEatingDead(true);
					candidate.setEatingDead(true);
				}
			}
			candidate.setCapturing(state.isCapturing());
			candidate.setGifting(state.isGifting());
			candidate.setBreaths(breathAfterPlay(point, color).size());
			// this.EyesAfterPlay(candidate, point, color);

			if (forTarget) {// 做眼方

				if (candidate.getBreaths() == 0) {// 不允许自杀
					continue;
				}
				if (candidate.getBreaths() == 1) {// 不能使目标块处于被打吃状态
					/**
					 * 有两种情况：<br/>
					 * 1. 做眼棋块送吃。<br/>
					 * 2. 送吃的是单子，不与原做眼棋块相邻。(比如盘角曲四的扑）
					 */
					Block targetBlock = this.getBlock(target);
					if (targetBlock.getBreathPoints().contains(point)) {
						continue;
					} else {

					}
				} else if (this.getBlankBlock(point).isEyeBlock()) {
					// bug here, only pass is left.
					// if (this.getBlankBlock(point).getNeighborMinimumBreath()
					// > 1) {
					// continue;// 眼位不粘，除非被打吃。
					// }
				}
			}
			if (candidate.getStep().getPoint() == Point.getPoint(7, 7, 5)) {
				if (log.isEnabledFor(Level.WARN))
					log.warn(candidate.getStep());
			}
			if (forTarget && state.isEating() == false) {
				// EyesAfterPlay(candidate, point, color);
				// replace by dynamic calculation.

				/**
				 * tricky bug, it takes 1 day.<br/>
				 * ##01,02,03,04,05,06,07 <br/>
				 * 01[_, _, B, _, _, _, _]01<br/>
				 * 02[_, _, B, _, _, _, _]02<br/>
				 * 03[_, _, B, _, _, B, W]03<br/>
				 * 04[B, B, B, B, B, _, _]04<br/>
				 * 05[B, W, B, _, B, B, W]05<br/>
				 * 06[B, W, W, B, W, W, W]06<br/>
				 * 07[B, W, W, _, W, _, _]07<br/>
				 * ##01,02,03,04,05,06,07 <br/>
				 * whoseTurn=Black<br/>
				 * root cause, we evaluate the eye effect by revert color, so
				 * [5,4]become invalid step.
				 */
				boolean valid = this.oneStepForward(candidate.getStep());
				if (valid) {
					Group group = this.getBlock(target).getGroup();
					EyeResult eyeRes = null;
					if (group != null) {
						eyeRes = this.getRealEyes(group, false);
					} else if (targets != null) {
						eyeRes = this.getRealEyes(targets, false);
					} else {
						eyeRes = this.getRealEyes(target, false);
					}
					int eyes = eyeRes.getRealEyes().size() * 2;
					eyes += eyeRes.getFakeEyes().size();
					candidate.setEyes(eyes);
				}
				if (valid
						|| this.getLastPoint() == candidate.getStep()
								.getPoint()) {
					this.oneStepBackward();
				}
				// this.initEyesAfterPlay_dynamic(candidate);
				candidate.setTigerMouths(this.tigerMouthAfterPlay(point, color)
						.size());
			} else if (forTarget == false && state.isEating() == false) {
				candidate.getStep().revertColor();
				// this.initEyesAfterPlay_dynamic(candidate);
				boolean valid = this.oneStepForward(candidate.getStep());
				if (valid) {
					Group group = this.getBlock(target).getGroup();
					EyeResult eyeRes = null;
					if (group != null) {
						eyeRes = this.getRealEyes(group, false);
					} else if (targets != null) {
						eyeRes = this.getRealEyes(targets, false);
					} else {
						eyeRes = this.getRealEyes(target, false);
					}
					int eyes = eyeRes.getRealEyes().size() * 2;
					eyes += eyeRes.getFakeEyes().size();
					candidate.setEyes(eyes);
				}
				if (valid
						|| this.getLastPoint() == candidate.getStep()
								.getPoint()) {
					this.oneStepBackward();
				}
				candidate.getStep().revertColor();

				candidate.setTigerMouths(this.tigerMouthAfterPlay(point,
						ColorUtil.enemyColor(color)).size());

				// candidate.setTigerMouths(this.tigerMouthAfterPlay(point,
				// color)
				// .size());
			}
			if (loop) {
				candidate.setLoopSuperior(loop);
			}
			cans.add(candidate);
		}
		Collections.sort(cans, new MakeEyeComparator());
		// if (forTarget) {
		// Collections.sort(cans, new MakeEyeComparator());
		// } else {
		// Collections.sort(cans, new BreakEyeComparator());
		// }

		// 先不考虑排序（优化）
		// for (Candidate candidate2 : cans) {
		// can.add(candidate2.getStep().getPoint());
		// }

		// if(this.liveSearch){
		//
		// }
		if (forTarget) {
			// || (this.noStep() == false && this.getLastStep().isGiveup() ==
			// true)) {
			/**
			 * 做活方可能需要考虑弃权，至少在形式双活时。正常情况下，加入也无害，因为别的着手已经可以做活。<br/>
			 * 另一方面攻击一方可以弃权.
			 */
			if (isDualLivePotential(target, scope) || cans.isEmpty()) {
				Candidate candidateP = new Candidate();
				candidateP.setStep(new Step(null, color, getShoushu() + 1));
				cans.add(0, candidateP);
			}
		} else if (this.noStep() == false
				&& this.getLastStep().isPass() == true) {
			// only consider pass as last option in case of potential dual live.
			if (isDualLivePotential(target, scope)) {
				Candidate candidateP = new Candidate();
				candidateP.setStep(new Step(null, color, getShoushu() + 1));
				cans.add(candidateP);
			}
			// Candidate candidateP = new Candidate();
			// candidateP.setStep(new Step(null, color, getShoushu() + 1));
			// cans.add(candidateP);//
			// cans.add(0, candidateP);// put at the beginning.
		}
		return cans;
	}

	/**
	 * single target block
	 * 
	 * @param target
	 * @param scope
	 * @param color
	 * @param forTarget
	 * @param loopSuperior
	 * @return
	 */
	public List<Candidate> getCandidate_forTarget(Point target,
			Set<Point> scope, boolean forTarget, boolean loopSuperior,
			boolean liveSearch) {
		int color = this.getColor(target);
		if (forTarget) {
			// OK.
		} else {
			color = ColorUtil.enemyColor(color);
		}

		List<Candidate> cans = new ArrayList<Candidate>();

		/**
		 * we need to extend the scope in case of one block of target group is
		 * eaten and we can eat back.--hot fix
		 * 
		 */
		if (this.noStep() == false && this.getLastStep().isPass() == false) {
			if (this.getBreaths(this.getLastPoint()) == 1) {
				scope.add(this.getBlock(this.getLastPoint()).getLastBreath());
			}
			if (this.getLastStep().getNeighborState().isEating()) {
				for (Block eaten : (getLastStep().getNeighborState()
						.getEatenBlocks())) {
					scope.addAll(eaten.allPoints);
				}
			}
		}
		// Point prohibitedP = getLastStep().getProhibittedPoint();
		// if (prohibitedP != null) {
		//
		// // }
		boolean loop;
		for (Point point : scope) {
			// because validate below has flaw without continue, we need check
			// here to ensure correctness.
			if (this.getColor(point) != Constant.BLANK) {
				continue;
			}
			/**
			 * 打劫的禁着点进行特殊处理：<br/>
			 * 劫材有利的话，通过大劫材（绝对劫材）提回劫。 劫材不利的话不考虑这个选择。
			 * 
			 */
			loop = false;
			if (this.validate(point, color) == false) {
				if (noStep())
					continue;
				Point prohibitedP = getLastStep().getProhibittedPoint();
				if (prohibitedP != null && prohibitedP == point) {
					if (loopSuperior) {
						loop = true;
						if (log.isEnabledFor(Level.WARN))
							log.warn("color=" + color + "劫材有利" + " 考虑 " + point
									+ "强行提回劫。");
					} else {
						if (log.isEnabledFor(Level.WARN))
							log.warn("color=" + color + "劫材不利，弃权，" + " 不考虑"
									+ point);
						// 找劫材，局部就是弃权。
						if (forTarget == false) { // avoid duplicate give up.???
							// Candidate candidate = new Candidate();
							// candidate.setStep(new Step(null, color,
							// this.shoushu + 1));
							// cans.add(candidate);
						}// target give up is done later.
						continue;
					}

				} else {

					continue;
				}
			}
			SimpleNeighborState state = this.getNeighborState_bigEye(point,
					color);
			Candidate candidate = new Candidate();
			candidate.setStep(new Step(point, color, this.shoushu + 1));
			candidate.setEating(state.isEating());

			if (state.isEating()) {
				if (forTarget == false) {
					// attacker eating target always make sense!
				} else {
					boolean justified = false;

					// 气数为一,又不能长气,必然可以被提子.
					for (Block block : state.getEatenBlocks()) {
						if (block.getMinBreathEnemyBlock().getBreaths() == 1) {
							justified = true;
							break;
						}
					}
					if (justified == false) {
						candidate.setEatingDead(true);
					}
				}
			}
			candidate.setCapturing(state.isCapturing());
			candidate.setGifting(state.isGifting());
			candidate.setBreaths(breathAfterPlay(point, color).size());
			// this.EyesAfterPlay(candidate, point, color);

			if (forTarget) {// 做眼方

				if (candidate.getBreaths() == 0) {// 不允许自杀
					continue;
				}
				if (candidate.getBreaths() == 1) {// 不能使目标块处于被打吃状态
					/**
					 * 有两种情况：<br/>
					 * 1. 做眼棋块送吃。<br/>
					 * 2. 送吃的是单子，不与原做眼棋块相邻。(比如盘角曲四的扑）
					 */
					Block targetBlock = this.getBlock(target);
					if (targetBlock.getBreathPoints().contains(point)) {
						continue;
					} else {

					}
				} else if (this.getBlankBlock(point).isEyeBlock()) {
					// bug here, only pass is left.
					// if (this.getBlankBlock(point).getNeighborMinimumBreath()
					// > 1) {
					// continue;// 眼位不粘，除非被打吃。
					// }
				}
			}

			if (forTarget && state.isEating() == false) {
				// EyesAfterPlay(candidate, point, color);
				// replace by dynamic calculation.

				/**
				 * tricky bug, it takes 1 day.<br/>
				 * ##01,02,03,04,05,06,07 <br/>
				 * 01[_, _, B, _, _, _, _]01<br/>
				 * 02[_, _, B, _, _, _, _]02<br/>
				 * 03[_, _, B, _, _, B, W]03<br/>
				 * 04[B, B, B, B, B, _, _]04<br/>
				 * 05[B, W, B, _, B, B, W]05<br/>
				 * 06[B, W, W, B, W, W, W]06<br/>
				 * 07[B, W, W, _, W, _, _]07<br/>
				 * ##01,02,03,04,05,06,07 <br/>
				 * whoseTurn=Black<br/>
				 * root cause, we evaluate the eye effect by revert color, so
				 * [5,4]become invalid step.
				 */
				boolean valid = this.oneStepForward(candidate.getStep());
				if (valid) {
					Group group = this.getBlock(target).getGroup();
					EyeResult eyeRes = null;
					if (group != null) {
						eyeRes = this.getRealEyes(group, false);
					} else {
						eyeRes = this.getRealEyes(target, false);
					}
					int eyes = eyeRes.getRealEyes().size() * 2;
					eyes += eyeRes.getFakeEyes().size();
					candidate.setEyes(eyes);
				}
				if (valid
						|| this.getLastPoint() == candidate.getStep()
								.getPoint()) {
					this.oneStepBackward();
				}
				// this.initEyesAfterPlay_dynamic(candidate);
				candidate.setTigerMouths(this.tigerMouthAfterPlay(point, color)
						.size());
			} else if (forTarget == false && state.isEating() == false) {
				candidate.getStep().revertColor();
				// this.initEyesAfterPlay_dynamic(candidate);
				boolean valid = this.oneStepForward(candidate.getStep());
				if (valid) {
					Group group = this.getBlock(target).getGroup();
					EyeResult eyeRes = null;
					if (group != null) {
						eyeRes = this.getRealEyes(group, false);
					} else {
						eyeRes = this.getRealEyes(target, false);
					}
					int eyes = eyeRes.getRealEyes().size() * 2;
					eyes += eyeRes.getFakeEyes().size();
					candidate.setEyes(eyes);
				}
				if (valid
						|| this.getLastPoint() == candidate.getStep()
								.getPoint()) {
					this.oneStepBackward();
				}
				candidate.getStep().revertColor();

				candidate.setTigerMouths(this.tigerMouthAfterPlay(point,
						ColorUtil.enemyColor(color)).size());

				// candidate.setTigerMouths(this.tigerMouthAfterPlay(point,
				// color)
				// .size());
			}
			if (loop) {
				candidate.setLoopSuperior(loop);
			}
			cans.add(candidate);
		}
		Collections.sort(cans, new MakeEyeComparator());
		// if (forTarget) {
		// Collections.sort(cans, new MakeEyeComparator());
		// } else {
		// Collections.sort(cans, new BreakEyeComparator());
		// }

		// 先不考虑排序（优化）
		// for (Candidate candidate2 : cans) {
		// can.add(candidate2.getStep().getPoint());
		// }

		Candidate candidatePass = new Candidate();
		candidatePass.setStep(new Step(null, color, getShoushu() + 1));
		if (liveSearch) {
			boolean dualLivePotential = isDualLivePotential(target, scope);
			log.warn("dualLivePotential=" + dualLivePotential);
			log.warn("target=" + target);
			log.warn("scope=" + scope);
			if (dualLivePotential) {

				if (forTarget) {
					/**
					 * 做活方可能需要考虑弃权，至少在形式双活时。<br/>
					 * 但是正常情况下，不得弃权,即使攻击方弃权也不得弃权.<br/>
					 * 这是为了让攻击方在计算中不需要紧外气
					 */
					cans.add(candidatePass);
				} else {
					if (this.noStep() == false
							&& this.getLastStep().isPass() == true) {
						// put at the beginning.若目标方已经弃权,可以弃权.得到双活结论.
						cans.add(0, candidatePass);
					} else {
						// 攻击方主动弃权则放在最后.
						cans.add(candidatePass);
					}
				}
			}

		} else { // dead search. target at cleaning up!

		}

		if (cans.isEmpty()) {
			cans.add(candidatePass);
		}
		return cans;
	}

	/**
	 * 两口内气作为双活的潜在条件.
	 * 
	 * @param target
	 * @param scope may contains more points than necessary during search.
	 * @return
	 */
	public boolean isDualLivePotential(Point target, Set<Point> scope) {
		Set<Point> breaths = new HashSet<Point>();
		Block targetBlock = getBlock(target);
		// safe copy
		breaths.addAll(targetBlock.getBreathPoints());
		breaths.retainAll(scope);
		if (breaths.size() != 2)
			return false;
		/**
		 * 这两口内气都是对方的送吃不入气点.<br/>
		 * scope may contains more points than necessary during search.
		 */
		for (Point point : breaths) {
			if (breathAfterPlay(point, targetBlock.getEnemyColor()).size() != 1)
				return false;
		}
		return true;
	}

	/**
	 * 已知不会提子。<br/>
	 * 事先判断某处落子是否可以眼，及其数量。
	 * 
	 * @param original
	 * @param color
	 * @return
	 */
	public void EyesAfterPlay(Candidate candidate, Point original, int color) {
		if (this.getColor(original) != Constant.BLANK) {
			throw new RuntimeException("getColor(original)!=Constant.BLANK");
		}

		BlankBlock blankBlock = this.getBlankBlock(original);
		if (blankBlock.isEyeBlock()) {
			if (blankBlock.getEyeColor() != color) {
				return;// break the original big eyes.
			} else {// make more small eyes from old big eye.

			}
		} else {// shared breath block.
			if (color == Constant.BLACK) {
				if (blankBlock.getWhiteBlocks().size() >= 2) {
					return; // cannot make eyes
				} else {
					// whether block is still linked to breath block after play.
					// for()
				}
			} else {
				if (blankBlock.getBlackBlocks().size() >= 2) {
					return; // cannot make eyes
				} else {

				}
			}

		}

		this.setColor(original, color);
		Set<Point> eyes = new HashSet<Point>();
		for (Delta delta : Constant.ADJACENTS) {
			Point neighbourPoint = original.getNeighbour(delta);
			if (neighbourPoint == null) {
				continue;
			}
			if (this.getColor(neighbourPoint) != Constant.BLANK)
				continue;
			if (this.isSinglePointEye(candidate, neighbourPoint, color)) {
				eyes.add(neighbourPoint);
			}
		}
		this.setColor(original, Constant.BLANK);
		candidate.setEyes(eyes.size());
		// return eyes;
	}

	public Candidate bigEyeFilledWithEnemy(Block block) {
		return bigEyeFilledWithEnemy(block, true);
	}

	/**
	 * TODO:
	 * 
	 * @deprecated Not mature!
	 * @param block
	 * @param includePureEye
	 * @return
	 */
	public Candidate bigEyeFilledWithEnemy(Block block, boolean includePureEye) {
		int color = block.getColor();
		int enemyColor = ColorUtil.enemyColor(color);
		Candidate can = new Candidate();
		Set<Point> eyes = new HashSet<Point>();
		int eyeSize = 0;

		/**
		 * simplified version, one enemy block with several breath block is
		 * treated as one eye!
		 */

		if (includePureEye) {
			for (BlankBlock blankB : block.getBreathBlocks()) {
				if (blankB.isEyeBlock()) {
					eyes.add(blankB.getBehalfPoint());
					eyeSize += blankB.getNumberOfPoint();
					continue;
				}
			}
		}

		for (Block blockE : block.getEnemyBlocks()) {
			boolean pointedEye = true;
			int virtualBlocks = 0;
			for (BlankBlock blankB : blockE.getBreathBlocks()) {
				for (Block nBlock : blankB.getNeighborBlocks()) {
					if (nBlock == block)
						continue;
					else if (nBlock == blockE) {
						continue;
					} else if (nBlock.color == enemyColor) {
						// TODO more than one enemy block!
						pointedEye = false;
						break;
					} else {// same color block.
							// have to be virtual connected.
						Set<Point> breaths = new HashSet<Point>();
						breaths.addAll(block.getBreathPoints());
						breaths.removeAll(nBlock.getBreathPoints());
						if (breaths.size() < block.getBreaths()) {
							virtualBlocks++;
							continue;
						} else {
							pointedEye = false;
							break;
						}
					}
				}
				if (pointedEye == false)
					break;
			}
			if (pointedEye == true) {
				// TODO
				eyes.add(blockE.getBehalfPoint());
				if (virtualBlocks > 0) {
					eyeSize += blockE.getNumberOfPoint();
					eyeSize -= virtualBlocks;
				} else {
					eyeSize += blockE.getNumberOfPoint();
				}

			}

		}
		can.setEyes(eyes.size());
		can.setCountEyePoint(eyeSize);
		return can;
	}

	/**
	 * 已知不会提子。<br/>
	 * 事先判断某处落子是否可以形成虎口。
	 * 
	 * @param original
	 * @param color
	 * @return
	 */
	public Set<Point> tigerMouthAfterPlay(Point original, int color) {
		if (this.getColor(original) != Constant.BLANK) {
			throw new RuntimeException(
					"getColor(original)!=Constant.BLANK+original" + original);
		}
		this.setColor(original, color);
		Set<Point> tigerMouth = new HashSet<Point>();
		for (Delta delta : Constant.ADJACENTS) {
			Point neighbourPoint = original.getNeighbour(delta);
			if (neighbourPoint == null) {
				continue;
			}
			if (this.getColor(neighbourPoint) != Constant.BLANK)
				continue;
			if (this.isTigerMouth(neighbourPoint)) {
				tigerMouth.add(neighbourPoint);
			}
		}
		this.setColor(original, Constant.BLANK);
		return tigerMouth;
	}

	/**
	 * 判断一个点是否是虎口。
	 * 
	 * @param point
	 * @return
	 */
	public boolean isTigerMouth(Point point) {
		int black = 0;
		int white = 0;
		int blank = 0;
		int outOfBound = 0;
		if (this.getBoardPoint(point).getColor() == ColorUtil.BLANK) {
			for (Delta delta : Constant.ADJACENTS) {
				Point neighbourPoint = point.getNeighbour(delta);
				if (neighbourPoint == null) {
					outOfBound++;
					continue;
				}

				if (this.getColor(neighbourPoint) == ColorUtil.BLACK)
					black++;
				else if (this.getColor(neighbourPoint) == ColorUtil.WHITE)
					white++;
				else if (this.getColor(neighbourPoint) == ColorUtil.BLANK)
					blank++;
				else
					outOfBound++;
			}
			if (blank == 1) {
				if (black + outOfBound == 3) {
					return true;
				} else if (white + outOfBound == 3) {
					return true;
				} else
					return false;
			} else
				return false;
		} else
			return false;
	}

	/**
	 * 仅仅能够识别单子眼，多子眼位会被识别成虎口！ （大眼死活计算可以够用！） <br/>
	 * change to cover non single point eyes
	 * 
	 * @param point
	 * @return
	 */
	public boolean isSinglePointEye(Candidate candidate, Point point,
			int eyeColor) {
		int black = 0;
		int white = 0;
		int blank = 0;
		int outOfBound = 0;
		if (this.getBoardPoint(point).getColor() != ColorUtil.BLANK) {
			return false;
		}
		for (Delta delta : Constant.ADJACENTS) {
			Point neighbourPoint = point.getNeighbour(delta);
			if (neighbourPoint == null) {
				outOfBound++;
				continue;
			}

			if (this.getColor(neighbourPoint) == ColorUtil.BLACK) {
				black++;
				if (this.getColor(neighbourPoint) != eyeColor) {
					return false;
				}
			} else if (this.getColor(neighbourPoint) == ColorUtil.WHITE) {
				white++;
				if (this.getColor(neighbourPoint) != eyeColor) {
					return false;
				}
			} else if (this.getColor(neighbourPoint) == ColorUtil.BLANK) {
				blank++;
				// break;
				continue;
			} else
				outOfBound++;
		}
		if (blank != 0) {// maybe bigger eyes.
			return false;
		}
		if (black + outOfBound == 4) {
			return true;
		} else if (white + outOfBound == 4) {
			return true;
		} else {
			return false;
		}

	}

	public boolean verify1() {
		Set<Block> blocks = new HashSet<Block>();
		byte row;
		byte column;
		short count = 0;
		for (row = 1; row <= boardSize; row++) {
			for (column = 1; column <= boardSize; column++) {
				if (getBoardPoint(row, column).getBlock() == null) {

				} else if (!blocks.contains(getBoardPoint(row, column)
						.getBlock())) {
					blocks.add(getBoardPoint(row, column).getBlock());
					count += getBoardPoint(row, column).getTotalNumberOfPoint();
					if (log.isDebugEnabled()) {
						log.debug("i=" + row + ",j=" + column + ",count="
								+ count + ";");
					}
				}
			}
		}
		return count == boardSize * boardSize;

	}

	/**
	 * one dead stone represents a block of stones.<br/>
	 * complex: need code like the capturing a block. NOT DONE!
	 */
	public void cleanupDeadStone(Set<Point> deadStone) {

		for (Point point : deadStone) {
			Block block = this.getBlock(point);
			block.removeEnemyBlocks_TwoWay();
			block.removeBreathBlocks_TwoWay();
			// block.removeFromAllBreathBlock();
		}

	}

	public FinalResult simpleCountScore() {
		int blank = 0;
		int white = 0;
		int whiteSpace = 0;
		int blackSpace = 0;
		int black = 0;
		int count;
		for (BasicBlock block : this.getAllBlocks()) {
			if (block.isBlank()) {
				BlankBlock blankBlock = (BlankBlock) block;
				if (blankBlock.isEyeBlock()) {
					if (blankBlock.isBlackEye()) {
						count = blankBlock.getNumberOfPoint();
						blackSpace += count;
						if (log.isEnabledFor(Level.WARN))
							log.warn("black space ["
									+ blankBlock.getBehalfPoint() + "] = "
									+ count);
					} else {
						count = blankBlock.getNumberOfPoint();
						whiteSpace += count;
						if (log.isEnabledFor(Level.WARN))
							log.warn("white space ["
									+ blankBlock.getBehalfPoint() + "] = "
									+ count);
					}
				} else {
					blank += blankBlock.getNumberOfPoint();
				}
			} else if (block.isBlack()) {
				count = block.getNumberOfPoint();
				black += count;

				if (log.isEnabledFor(Level.WARN))
					log.warn("black block [" + block.getBehalfPoint() + "] = "
							+ count);
			} else {

				count = block.getNumberOfPoint();
				white += count;
				if (log.isEnabledFor(Level.WARN))
					log.warn("white block [" + block.getBehalfPoint() + "] = "
							+ count);
			}
		}
		if (log.isEnabledFor(Level.WARN))
			log.warn("black = " + black);
		if (log.isEnabledFor(Level.WARN))
			log.warn("blackSpace = " + blackSpace);
		if (log.isEnabledFor(Level.WARN))
			log.warn("white = " + white);
		if (log.isEnabledFor(Level.WARN))
			log.warn("whiteSpace = " + whiteSpace);
		if (log.isEnabledFor(Level.WARN))
			log.warn("blank = " + blank);

		if (black + white + blank + blackSpace + whiteSpace != 361)
			throw new RuntimeException("black+white+blank = "
					+ (black + white + blank));
		FinalResult res = new FinalResult(black + blackSpace, white
				+ whiteSpace, blank);
		return res;
		// return black - white;
	}

	/**
	 * if we need to get those eye info by playing, maybe we should adopt the
	 * static calculation strategy for other attributes.<br/>
	 * we ever suspect the dynamic code might break the data structure, and it
	 * turned out caused by other bug (eye block is changed by mistake) TODO
	 * 
	 * @param candidate
	 * @deprecated
	 */
	public void initEyesAfterPlay_dynamic(Candidate candidate) {

		Point original = candidate.getStep().getPoint();
		int color = candidate.getStep().getColor();
		if (this.getColor(original) != Constant.BLANK) {
			throw new RuntimeException("getColor(original)!=Constant.BLANK");
		}
		Set<Point> eyes = new HashSet<Point>();
		int eyePointCount = 0;
		try {
			boolean valid = this.oneStepForward(original, color);
			if (valid == false) {
				if (this.getLastStep().getStep().getPoint() == original) {
					this.oneStepBackward();
				}
				return;
			}
		} catch (RuntimeException e) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(this.getBoardColorState().getStateString());
			if (log.isEnabledFor(Level.WARN))
				log.warn("roig=" + candidate);
		}
		/**
		 * TODO: pattern recognition to simplify the calculation. no need to
		 * consider the details in the context,
		 */
		// if (this.isAlreadyLive_dynamic(original)) {
		// candidate.setBecomeLive(true);
		// }
		if (this.noStep())
			return;
		SimpleNeighborState neighborState = this.getLastStep()
				.getNeighborState();
		Candidate bigEyeFilledWithEnemy = this.bigEyeFilledWithEnemy(this
				.getBlock(neighborState.getOriginal()));

		// if (neighborState.isEating()) {
		// for (Block blockE : getLastStep().getEatenBlocks()) {
		// eyes.add(blockE.getBehalfPoint());
		// eyePointCount += blockE.getNumberOfPoint();
		// }
		//
		// }
		//
		// Set<BlankBlock> tempB = new HashSet<BlankBlock>();
		//
		// tempB.addAll(this.getBlock(neighborState.getOriginal())
		// .getBreathBlocks());
		//
		// for (BlankBlock blankB : tempB) {
		// if (blankB.isEyeBlock()) {
		// eyes.add(blankB.getBehalfPoint());
		// if (blankB.getNeighborBlocks().size() == 1) {
		// eyePointCount += blankB.getPoints().size();
		// } else {
		// eyePointCount += blankB.getPoints().size();
		// eyePointCount -= 2;
		// }
		// } else {// identify the eye with enemy point
		//
		// // TODO quick simple version, need generalization
		// boolean pointedEye = true;
		// for (Block enemyB : blankB.getNeighborBlocks()) {
		// if (enemyB.getColor() != neighborState.getFriendColor()) {
		// if (enemyB.getEnemyBlocks().size() > 1) {
		// pointedEye = false;
		// } else if (enemyB.getBreaths() >= this
		// .getBreaths(neighborState.getOriginal())) {
		// pointedEye = false;
		// }
		// }
		// }
		// if (pointedEye) {
		// eyes.add(blankB.getBehalfPoint());
		// if (blankB.getNeighborBlocks().size() == 1) {
		// eyePointCount += blankB.getPoints().size();
		// } else {
		// eyePointCount += blankB.getPoints().size();
		// eyePointCount -= 2;
		// }
		// }
		// }
		// }
		// } else if (neighborState.isOriginalBlankBlockDisappear() == true) {
		// //no blank block, but still might have eyes with enemy point
		// for (Block blockE : getLastStep().getEatenBlocks()) {
		// eyes.add(blockE.getBehalfPoint());
		// eyePointCount += blockE.getNumberOfPoint();
		// }
		//
		// } else if (neighborState.isOriginalBlankBlockDisappear() == false) {
		// BlankBlock blankBlock = this.getLastStep().getOriginalBlankBlock();
		// if (blankBlock.isEyeBlock()) {
		// eyes.add(blankBlock.getBehalfPoint());
		// eyePointCount += blankBlock.getPoints().size();
		// }
		//
		// }

		// eyePointCount = bigEyeFilledWithEnemy.getCountEyePoint();
		candidate.setEyes(bigEyeFilledWithEnemy.getEyes());
		candidate.setCountEyePoint(bigEyeFilledWithEnemy.getCountEyePoint());

		/**
		 * TODO: change: also calculate the breath increased.
		 */

		try {
			this.oneStepBackward();
		} catch (RuntimeException e) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(this.getBoardColorState().getStateString());
			if (log.isEnabledFor(Level.WARN))
				log.warn("roig=" + candidate);
		}

		if (neighborState.getFriendBlockNumber() > 0) {
			int eyeC = candidate.getEyes();
			int count = candidate.getCountEyePoint();
			for (Block blockF : neighborState.getFriendBlocks()) {
				// duplicate counting if two block share same eye
				Candidate can = this.bigEyeFilledWithEnemy(blockF);
				eyeC -= can.getEyes();
				count -= can.getCountEyePoint();
				if (eyeC <= 0)
					break;
			}
			candidate.setEyes(eyeC);
			candidate.setCountEyePoint(count);
		}
	}

	/**
	 * 得到所有的(狭义的)真眼.处理90%的情况.<br/>
	 * 大眼在这里不算真眼。<br/>
	 * 
	 * 眼位可能还没有定型，比如半只眼是指先手可以有一眼，后手则无眼。<br/>
	 * 因此增加先后手的参数targetFirst。
	 * 
	 * bug here, if the eye is pointed(点过的眼) by enemy! <br/>
	 * Change the algorithm.<br/>
	 * 点过的眼,不再识别为眼,而是作为一个敌方的弱块来参与对杀.处于未终结状态,但是可能已经确定<br/>
	 * 直二点过之后作为一个自杀不入气等价于一个眼. <br/>
	 * 
	 * @param target
	 *            any point in the target block<br/>
	 * @param targetFirst
	 *            是否轮到目标棋块方走。
	 * @return
	 */
	public EyeResult getRealEyes(Point target, boolean targetFirst, EyeResult es) {
		Set<Point> fakeEyes = es.getFakeEyes();
		Set<Point> halfEyes = es.getHalfEyes();
		Set<Point> realEyes = es.getRealEyes();
		// big eyes are count duplicated for judging potential live.
		Set<Point> bigEyes = es.getBigEyes();

		Block block = getBlock(target);
		int myColor = block.getColor();
		Point eyePoint = null;
		for (BlankBlock blankBlock : block.getBreathBlocks()) {
			if (blankBlock.isEyeBlock() == false) {// 公气
				boolean externalSingleBlock = true;
				int enemyBreath = 0;
				for (Block tempB : blankBlock.getNeighborBlocks()) {
					if (tempB == block)
						continue;
					for (Block eB : tempB.getEnemyBlocks()) {
						if (eB != block) {
							externalSingleBlock = false;
							break;
						}
					}
					enemyBreath = tempB.getBreaths();
					for (BlankBlock bB : tempB.getBreathBlocks()) {
						if (bB == blankBlock)
							continue;
						if (bB.isEyeBlock()) {// 大眼内的敌方有眼位。
							externalSingleBlock = false;
							break;
						}
						for (Block extB : bB.getNeighborBlocks()) {
							if (extB != block && extB != tempB) {
								externalSingleBlock = false;
								break;
							}
						}
					}
					if (externalSingleBlock == false)
						break;
				}
				if (externalSingleBlock == true) {
					// 小棋盘上的假的大眼.
					if (block.getNumberOfPoint() < blankBlock
							.getNumberOfPoint())
						;
					else if (blankBlock.getNumberOfPoint() == 1
							&& enemyBreath == 1)
						// ;
						// 不作为眼.需要搜索才能识别出眼 why?
						// line of there is broken in middle.
						realEyes.add(blankBlock.getBehalfPoint());
				} else {
					continue;
				}
				// if (blankBlock.getNumberOfPoint() == 1)
				continue;
			}

			// is eye!
			if (blankBlock.getNumberOfPoint() != 1) {// 大眼TODO
				if (this.isBigEyeComplete(blankBlock)) {
					realEyes.add(blankBlock.getBehalfPoint());
				}
				if (blankBlock.getNumberOfPoint() >= 3) {
					bigEyes.add(blankBlock.getBehalfPoint());
				}
				continue;
			}

			eyePoint = blankBlock.getUniquePoint();
			this.handleSinglePointEye(block, eyePoint, fakeEyes, halfEyes,
					realEyes);
		}

		/**
		 * 将真眼和半眼周围块的真眼合并。
		 */

		/**
		 * 检查气点中是否有真眼。
		 */
		// for (Point breathPoint : block.getAllBreathPoints()) {
		// if (isSameBlockEye(block, breathPoint, myColor)) {
		// eyes.add(breathPoint);
		// }
		// }
		return es;
	}

	public boolean isRealSingleEye(Block targetBlock, Point eyePoint) {
		Set<Point> fakeEyes = new HashSet<Point>();
		Set<Point> halfEyes = new HashSet<Point>();
		Set<Point> realEyes = new HashSet<Point>();
		handleSinglePointEye(targetBlock, eyePoint, fakeEyes, halfEyes,
				realEyes);
		return realEyes.contains(eyePoint);
	}

	/**
	 * 判断一个单点眼是否为真眼、假眼、或者半眼(后手眼)，通过查看九宫顶点的方式来确定。
	 * 
	 * @param targetBlock
	 * @param eyePoint
	 * @param fakeEyes
	 * @param halfEyes
	 * @param realEyes
	 */
	public void handleSinglePointEye(Block targetBlock, Point eyePoint,
			Set<Point> fakeEyes, Set<Point> halfEyes, Set<Point> realEyes) {
		int friend = 0;
		int enemy = 0;
		int blank = 0;

		int myColor = targetBlock.getColor();
		Set<Point> enemys = new HashSet<Point>();
		for (Delta delta : Constant.SHOULDERS) {
			Point shoulder = eyePoint.getNeighbour(delta);
			if (shoulder == null)
				continue;

			if (this.getColor(shoulder) == myColor) {
				friend++;
			} else if (this.getColor(shoulder) == Constant.BLANK) {
				int breaths = this.breathAfterPlay(shoulder,
						targetBlock.getEnemyColor()).size();
				if (breaths < 1) {// 对方不入气或者自提。
					friend++;// 空白点由本方控制,计成本方点.
				} else if (breaths == 1) {
					/**
					 * 有几种可能： 1. 有一个扑的劫材，我方提子即可。<br/>
					 * 2. 对方提子后，我方也提子，但是对方仍可提子，此为打劫。<br/>
					 * 3. 对方扑后，我方提子；对方打二还一。 <br/>
					 * 4. 对方扑入后成为打劫<br/>
					 * 只有情况2，这一点我们没有占有。
					 */
					// TODO 打劫的处理
					friend++;// 先假设劫材有利，空白点由本方控制,计成本方点.
				} else {
					/**
					 * [B, _, B, B]<br/>
					 * [B, B, _, _]<br/>
					 * [_, _, _, _]<br/>
					 * [_, _, _, B]<br/>
					 */
					if (this.getBlankBlock(shoulder).isEyeBlock()) {
						friend++;
					} else {
						blank++;
					}
				}
			} else {// enemy
				enemys.add(shoulder);
				enemy++;
			}
		}

		if (eyePoint.isCenterEye()) {// center： friend + enemy + blank == 4
			if (enemy >= 2) {// TODO 进一步考虑能否攻击卡眼的点.
				fakeEyes.add(eyePoint);
			} else if (enemy == 1) {
				if (blank >= 2) {
					fakeEyes.add(eyePoint);
				} else if (blank == 1) {
					halfEyes.add(eyePoint);
				} else if (blank == 0) {
					realEyes.add(eyePoint);
				}

			} else if (enemy == 0) {

				if (blank >= 4) {
					fakeEyes.add(eyePoint);
				} else if (blank == 3) {
					halfEyes.add(eyePoint);
				} else if (blank == 2) {
					realEyes.add(eyePoint);
				} else {// fix a bug here.
					realEyes.add(eyePoint);
				}
			}
		} else if (eyePoint.isBorderEye()) {
			// boarder： friend + enemy + blank == 2
			if (enemy == 2) {
				fakeEyes.add(eyePoint);
			} else if (enemy == 1) {
				if (this.getBlock(enemys.iterator().next()).getBreaths() == 1) {
					halfEyes.add(eyePoint);
				} else {
					fakeEyes.add(eyePoint);
				}
			} else if (blank == 0) {
				realEyes.add(eyePoint);
			} else if (blank == 1) {
				halfEyes.add(eyePoint);
			} else if (blank == 2) {
				fakeEyes.add(eyePoint);
			}
		} else if (eyePoint.isCornerEye()) {
			// corner： friend + enemy + blank == 1
			if (friend == 1) { // 已经成眼
				realEyes.add(eyePoint);
			} else if (enemy == 1) {// 已经破眼
				fakeEyes.add(eyePoint);
			} else if (blank == 1) {// 后手眼
				halfEyes.add(eyePoint);
			}
		}

	}

	public EyeResult getRealEyes(Group group, boolean targetFirst) {
		EyeResult es = new EyeResult();
		es.init();

		for (Block block : group.getBlocks()) {
			getRealEyes(block.getBehalfPoint(), targetFirst, es);
		}
		return es;
	}

	public EyeResult getRealEyes(Set<Point> targets, boolean targetFirst) {
		EyeResult es = new EyeResult();
		es.init();
		for (Point target : targets) {
			getRealEyes(target, targetFirst, es);
		}
		return es;
	}

	public EyeResult getRealEyes(Point target, boolean targetFirst) {
		EyeResult es = new EyeResult();
		es.init();
		return getRealEyes(target, targetFirst, es);

	}

	/**
	 * 大眼的眼位是否完整 <br/>
	 * 最基本的情况是由一个块围成.
	 * 
	 * @return
	 */
	public boolean isBigEyeComplete(BlankBlock eyeBlock) {
		if (eyeBlock.getNeighborBlocks().size() == 1)
			return true;

		for (Point point : eyeBlock.getPoints()) {
			for (Delta delta : Constant.SHOULDERS) {
				Point shoulder = point.getNeighbour(delta);
				if (shoulder == null)
					continue;
				int color = this.getColor(shoulder);
				if (color == eyeBlock.getEyeColor())
					continue;
				else if (color == eyeBlock.getEyeEnemyColor())
					return false;
				else if (color == Constant.BLANK) {
					// 空白点看是否也是眼位。TODO：考虑虎口之类。
					if (this.getBlankBlock(shoulder).isEyeBlock())
						continue;
					else if (this.isTigerMouth(shoulder))
						continue;
					else
						return false;

				}
			}
		}
		return true;

	}

	/**
	 * 初始(刚落一子时)的大眼.成眼棋块的子数还不到眼块子数的一半。<br/>
	 * 常规的大眼需要六子或者更多。
	 */
	public boolean isBigEyeFullySurround(BlankBlock blankBlock) {
		int surround = 0;
		for (Point breath : blankBlock.getPoints()) {
			for (Delta delta : Constant.ADJACENTS) {
				Point surr = breath.getNeighbour(delta);
				if (surr == null)
					continue;
				if (this.getColor(surr) != Constant.BLANK)
					surround++;
			}
		}
		// bug here, corner single eye has surround = 2
		// if (surround < 3 || surround * 2 < blankBlock.getNumberOfPoint()) {
		if (surround <= 1 || surround < blankBlock.getNumberOfPoint()) {
			return false;
		} else
			return true;
	}

	public SimpleGoManual getSimpleGoManual() {
		SimpleGoManual manual = new SimpleGoManual(getInitState(),
				this.getInitTurn());
		// Point step;
		for (StepMemo memo : getStepHistory().getAllSteps()) {
			manual.addStep(memo.getStep());
		}
		return manual;
	}
}
