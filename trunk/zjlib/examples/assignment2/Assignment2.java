/* This file is part of ZJLib, a library written in java to support 
 the implementation of specifications written in Z-Notation in Java 5.
 
 Copyright (C) 2007  Moritz Eysholdt <Moritz@Eysholdt.de>

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package assignment2;

import edu.uwlax.cs.z.Z;
import edu.uwlax.cs.z.ZPrimitive;
import edu.uwlax.cs.z.ZSet;
import edu.uwlax.cs.z.ZShellUI;
import edu.uwlax.cs.z.Z.ZPredicate1;
import edu.uwlax.cs.z.Z.ZSelect1;
import edu.uwlax.cs.z.Z.ZPredicate2;

public class Assignment2 {

	public static class PLAYER extends ZPrimitive {
	}

	public static class Team extends ZPrimitive {
		public ZSet<PLAYER> players = Z.Set();

		public String toString() {
			return getName() + " player:" + players;
		}
	}

	public static class League {
		public ZSet<Team> teams = Z.Set();

		public boolean checkInvariants() {
			assert Z.forAll(teams, teams, new ZPredicate2<Team, Team>() {
				public boolean condition(Team t1, Team t2) {
					return !t1.equals(t2);
				}

				public boolean predicate(Team t1, Team t2) {
					return t1.players.intersection(t2.players).isEmpty();
				}
			});
			assert Z.forAll(teams, teams, new ZPredicate2<Team, Team>() {
				public boolean predicate(Team t1, Team t2) {
					return t1.getName().equals(t2.getName()) == (t1 == t2);
				}
			});
			return true;
		}

		public void addTeam(final Team team) {
			/* preparation */
			assert checkInvariants();
			ZSet<Team> teamsp = teams.clone();

			/* preconditions */
			assert Z.forAll(teams, new ZPredicate1<Team>() {
				public boolean predicate(Team t) {
					return t.players.intersection(team.players).isEmpty();
				}
			});
			assert !teams.isMember(team);

			/* implementation */
			teamsp.add(team);

			/* postconditions */
			assert teamsp.equals(teams.union(Z.Set(team)));

			/* finish */
			teams = teamsp;
			assert checkInvariants();
		}

		public void deleteTeam(Team team) {
			/* preparation */
			assert checkInvariants();
			ZSet<Team> teamsp = teams.clone();

			/* preconditions */
			/* none */

			/* implementation */
			teamsp.remove(team);

			/* postconditions */
			assert teamsp.equals(teams.substract((Z.Set(team))));

			/* finish */
			teams = teamsp;
			assert checkInvariants();
		}

		public void deletePlayer(final PLAYER player) {
			/* preparation */
			assert checkInvariants();
			final ZSet<Team> teamsp = teams.clone();

			/* preconditions */
			/* none */

			/* implementation */
			for (Team t : teamsp)
				if (t.players.isMember(player))
					t.players.remove(player);

			/* postconditions */
			Z.exists(teams, new ZPredicate1<Team>() {
				public boolean condition(Team t) {
					return t.players.isMember(player);
				}

				public boolean predicate(Team t) {
					Team n = ZPrimitive.getAnonymousInstance(Team.class, t
							.getName());
					n.players = t.players.difference(Z.Set(player));
					return teamsp.equals(teams.difference(Z.Set(t).union(
							Z.Set(n))));
				}
			});

			/* finish */
			teams = teamsp;
			assert checkInvariants();
		}

		public int getPlayerCountForTeam(final String teamname) {
			/* preparation */
			assert checkInvariants();
			final int count;

			/* preconditions */
			/* none */

			/* implementation */
			count = ZPrimitive.getInstance(Team.class, teamname).players
					.cardinality();

			/* postconditions */
			assert Z.exists(teams, new ZPredicate1<Team>() {

				public boolean condition(Team t) {
					return t.getName().equals(teamname);
				}

				public boolean predicate(Team t) {
					return count == t.players.cardinality();
				}
			});

			/* finish */
			assert checkInvariants();
			return count;
		}

		public int getPlayerCount() {
			/* preparation */
			final int count;
			assert checkInvariants();
			/* preconditions */
			/* none */

			/* implementation */
			int c = 0;
			for (Team t : teams)
				c += t.players.cardinality();
			count = c;

			/* postconditions */
			assert count == Z.Set(teams, new ZSelect1<Team, PLAYER>() {
				public ZSet<PLAYER> select(Team t) {
					return t.players;
				}
			}).cardinality();

			/* finish */
			assert checkInvariants();
			return count;
		}
	}

	public static void main(String[] args) {
		ZShellUI ui = new ZShellUI();
		League league = new League();
		createTestData(league);
		ui.setZClass(league);
		ui.runShell();
	}

	private static void createTestData(League l) {
		Team t1 = ZPrimitive.getInstance(Team.class, "Sharks");
		Team t2 = ZPrimitive.getInstance(Team.class, "Lions");
		Team t3 = ZPrimitive.getInstance(Team.class, "Bears");
		t1.players.add(ZPrimitive.getInstance(PLAYER.class, "Fritz Meyer"));
		t1.players.add(ZPrimitive.getInstance(PLAYER.class, "Max Mustermann"));
		t1.players.add(ZPrimitive.getInstance(PLAYER.class, "John Doe"));
		t2.players.add(ZPrimitive.getInstance(PLAYER.class, "Fritz"));
		t2.players.add(ZPrimitive.getInstance(PLAYER.class, "Hans"));
		t3.players.add(ZPrimitive.getInstance(PLAYER.class, "Thomas"));
		t3.players.add(ZPrimitive.getInstance(PLAYER.class, "Michael"));
		l.teams.add(t1);
		l.teams.add(t2);
		l.teams.add(t3);
	}

}
