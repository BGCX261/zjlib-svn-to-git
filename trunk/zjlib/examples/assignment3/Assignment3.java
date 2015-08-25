package assignment3;

import edu.uwlax.cs.z.Z;
import edu.uwlax.cs.z.ZPrimitive;
import edu.uwlax.cs.z.ZSet;
import edu.uwlax.cs.z.ZShellUI;
import edu.uwlax.cs.z.Z.ZPredicate1;
import edu.uwlax.cs.z.Z.ZPredicate2;

public class Assignment3 {

	public static void main(String[] args) {
		ZShellUI ui = new ZShellUI();
		ui.setZClass(new League());
		ui.runShell();
	}

	public static class PLAYER extends ZPrimitive {
	}

	public static class NAME extends ZPrimitive {
	}

	public static class Team extends ZPrimitive {
		public NAME name = new NAME();

		public ZSet<PLAYER> players = Z.Set();

		protected void updateName(String name) {
			this.name.setName(name);
		}
	}

	public static class League {
		public ZSet<Team> teams;

		public void checkInvariants() {
			assert Z.forAll(teams, teams, new ZPredicate2<Team, Team>() {
				public boolean predicate(Team arg0, Team arg1) {
					return arg0.players.intersection(arg1.players).isEmpty();
				}
			});
			assert Z.forAll(teams, teams, new ZPredicate2<Team, Team>() {
				public boolean predicate(Team arg0, Team arg1) {
					return arg0.name.equals(arg1.name) == (arg0 == arg1);
				}
			});
		}

		public void addTeam(final Team team) {
			/* begin preconditions */
			assert Z.forAll(teams, new ZPredicate1<Team>() {
				public boolean predicate(Team arg0) {
					return arg0.players.intersection(team.players).isEmpty();
				}
			});
			assert !teams.isMember(team);
			/* end preconditions */
			ZSet<Team> teamsp = teams.clone();
			/* begin implementation */
			// (...)
			/* end implementation */
			/* begin postconditions */
			assert teamsp.equals(teams.union(Z.Set(team)));
			/* end postconditions */
			teams = teamsp;
		}

		public void deleteTeam(Team team) {
			ZSet<Team> teamsp = teams.clone();
			/* begin implementation */
			// (...)
			/* end implementation */
			/* begin postconditions */
			assert teamsp.equals(teams.substract((Z.Set(team))));
			/* end postconditions */
			teams = teamsp;
		}
	}
}
