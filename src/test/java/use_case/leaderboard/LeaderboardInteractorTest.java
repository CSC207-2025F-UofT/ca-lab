package use_case.leaderboard;

import data_access.InMemoryUserDataAccessObject;
import entity.*;

import java.util.ArrayList;
import java.util.Random;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test methods for the leaderboard use case.
 * 100% coverage of the leaderboard use case.
 */
public class LeaderboardInteractorTest {

    private InMemoryUserDataAccessObject simulatedMemory;
    private final UserFactory factory = new UserFactory();
    private final Random random = new Random();
    private final int usersPerPage = LeaderboardInteractor.getUSERS_PER_PAGE();


    // Random constants:

    // Natural number to get a multiple of USERS_PER_PAGE:
    private final int rMultiple =
            1 + random.nextInt(5);

    // Additional natural number less than USERS_PER_PAGE
    // of user objects to add to memory without making the last page full:
    private final int rExtra =
            1 + random.nextInt(usersPerPage - 2);

    // Nonnegative integer of user objects to store in memory:
    private final int rArbitrary =
            random.nextInt(usersPerPage * 5);

    // Additional large natural number to execute an unrealistic page:
    private final int rExtreme =
            1 + random.nextInt(100);


    // Helper methods:

    /**
     * Execute the change page function with the given custom
     * presenter and new page.
     * @param presenter custom presenter for testing.
     * @param newPage new page to pass in.
     */
    private void executeTest(LeaderboardOutputBoundary presenter, int newPage) {
        LeaderboardInputBoundary interactorPage =
                new LeaderboardInteractor(simulatedMemory, presenter);
        interactorPage.changePage(
                new ChangePageInputData(newPage)
        );
    }

    /**
     * Populate memory with a specified number of basic user objects.
     * Return a checklist of their names to check off later.
     * @param numUsers number of user objects to store in memory.
     * @return checklist of the user object names that were stored.
     */
    private ArrayList<String> getNamesChecklist(int numUsers) {

        String currName;
        ArrayList<String> namesChecklist = new ArrayList<>();

        for (int i = 0; i < numUsers; i++) {
            currName = "Sunny" + i;

            simulatedMemory.save(
                    factory.create(currName, "password")
            );

            namesChecklist.add(currName);
        }

        return namesChecklist;
    }

    /**
     * Check if the user objects in the sublist are from the checklist.
     * Remove the ones seen and return the updated checklist.
     * @param userRankPairs resulting sublist of pairs from the use case.
     * @param namesChecklist current checklist of names.
     */
    private void checkOffNames(
            ArrayList<Object[]> userRankPairs,
            ArrayList<String> namesChecklist
    ) {
        String currName;
        for (Object[] pair : userRankPairs) {

            currName = ((User) pair [0]).getName();
            assertTrue(
                    namesChecklist.contains(currName)
            );

            namesChecklist.remove(currName);
        }
    }

    /**
     * Initialise a distinct simulated memory for each test.
     */
    @BeforeEach
    void setup() {
        simulatedMemory = new InMemoryUserDataAccessObject();
    }


    // Success tests:

    /**
     * The resulting new page number should be the same page
     * number passed in the input data.
     */
    @Test
    void returnsSamePage() {
        // Let there be the minimum number of user objects for 2
        // pages in memory to demonstrate multiple pages.
        for (int i = 0; i < 2 * usersPerPage + 1; i++) {
            simulatedMemory.save(
                    factory.create("Sunny" + i, "password")
            );
        }


        // Custom presenter(s) for testing:
        LeaderboardOutputBoundary presenterPage1 = new LeaderboardOutputBoundary() {

            @Override
            public void changePagePrepareSuccessView(ChangePageOutputData results) {
                assertEquals(1, results.getNewPage());
            }

            @Override
            public void changePagePrepareFailedView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        LeaderboardOutputBoundary presenterPage2 = new LeaderboardOutputBoundary() {

            @Override
            public void changePagePrepareSuccessView(ChangePageOutputData results) {
                assertEquals(2, results.getNewPage());
            }

            @Override
            public void changePagePrepareFailedView(String error) {
                fail("Use case failure is unexpected.");
            }
        };


        // Execute test(s).
        executeTest(presenterPage1, 1);
        executeTest(presenterPage2, 2);
    }

    /**
     * If there are no user objects in memory, the result for
     * page 1 should return the empty list.
     */
    @Test
    void emptyFirstPage() {
        // Do not store any user objects in memory.

        // Custom presenter(s) for testing:
        LeaderboardOutputBoundary presenter = new LeaderboardOutputBoundary() {

            @Override
            public void changePagePrepareSuccessView(ChangePageOutputData results) {
                assertTrue(results.getUserRankPairs().isEmpty());
            }

            @Override
            public void changePagePrepareFailedView(String error) {
                fail("Use case failure is unexpected.");
            }
        };


        // Execute test(s).
        executeTest(presenter, 1);
    }

    /**
     * If there is one user object in memory, the result for page
     * 1 should return a sublist with one pair of that user and
     * rank 1.
     */
    @Test
    void oneUserObject() {
        // Store a single user object in memory.
        simulatedMemory.save(
                factory.create("Sunny", "password")
        );


        // Custom presenter(s) for testing:
        LeaderboardOutputBoundary presenter = new LeaderboardOutputBoundary() {

            @Override
            public void changePagePrepareSuccessView(ChangePageOutputData results) {
                ArrayList<Object[]> userRankPairs = results.getUserRankPairs();

                // Sublist should have only one pair element.
                assertEquals(1, userRankPairs.size());

                Object[] pair = userRankPairs.get(0);

                // The user in the pair should be the one in memory (checking the name is enough).
                assertEquals("Sunny", ( (User) pair[0] ).getName());

                // The rank in the pair should be 1.
                assertEquals(1, (int) pair[1]);
            }

            @Override
            public void changePagePrepareFailedView(String error) {
                fail("Use case failure is unexpected.");
            }
        };


        // Execute test(s).
        executeTest(presenter, 1);
    }

    /**
     * If there are multiple user objects in memory with their
     * scores in random order, the resulting sublist will always be
     * sorted by user objects scores from MOST TO LEAST.
     */
    @Test
    void orderedByRank() {
        // Store multiple user objects with random unordered scores in memory.
        for (int i = 0; i < usersPerPage; i++) {
            simulatedMemory.save(
                    factory.create("Sunny" + i, "password",
                            random.nextInt(100), "", "")
            );
        }


        // Custom presenter(s) for testing:
        LeaderboardOutputBoundary presenter = new LeaderboardOutputBoundary() {

            @Override
            public void changePagePrepareSuccessView(ChangePageOutputData results) {
                ArrayList<Object[]> userRankPairs = results.getUserRankPairs();

                // For all pairs in the sublist, its user object
                // should have a score that is at least the next
                // user object's score.
                for (int i = 0; i < userRankPairs.size() -1; i++) {
                    assertTrue(
                            ( (User) userRankPairs.get(i) [0] ).getScore() >=
                                    ( (User) userRankPairs.get(i + 1) [0] ).getScore()
                    );
                }
            }

            @Override
            public void changePagePrepareFailedView(String error) {
                fail("Use case failure is unexpected.");
            }
        };


        // Execute test(s).
        executeTest(presenter, 1);
    }

    /**
     * If there are multiple user objects in memory with equal
     * score but different names, the resulting sublist will
     * always be sorted by user objects name ALPHANUMERICALLY.
     */
    @Test
    void orderedByName() {
        // Store multiple user objects in memory with the same score but different name.
        // Use a predefined sample space for simplicity and since names must be unique.
        ArrayList<String> nameSample = new ArrayList<>();
        nameSample.add("Annie");
        nameSample.add("Ivan");
        nameSample.add("Katy");
        nameSample.add("Shaun");
        nameSample.add("Shelley");
        nameSample.add("Sunny");

        String currName;
        for (int i = 0; i < usersPerPage; i++) {
            currName = nameSample.get(
                    random.nextInt(nameSample.size())
            );
            nameSample.remove(currName);

            simulatedMemory.save(
                    factory.create(currName, "password")
            );
        }


        // Custom presenter(s) for testing:
        LeaderboardOutputBoundary presenter = new LeaderboardOutputBoundary() {

            @Override
            public void changePagePrepareSuccessView(ChangePageOutputData results) {
                ArrayList<Object[]> userRankPairs = results.getUserRankPairs();

                // For all pairs in the sublist, its user object
                // should have a name that is greater
                // alphanumerically than the next user object's name.
                // i.e. First name should have lower value than second name.
                // i.e. First name compared to second name should yield negative.
                for (int i = 0; i < userRankPairs.size() -1; i++) {
                    assertTrue(
                            ( (User) userRankPairs.get(i) [0] ).getName()
                                    .compareTo(
                                            ( (User) userRankPairs.get(i + 1) [0] ).getName()
                                    )
                                    < 0
                    );
                }
            }

            @Override
            public void changePagePrepareFailedView(String error) {
                fail("Use case failure is unexpected.");
            }
        };


        // Execute test(s).
        executeTest(presenter, 1);
    }

    /**
     * If there are USERS_PER_PAGE user objects in
     * memory, the result for page 1 should return a sublist
     * with USERS_PER_PAGE pairs of those exact user
     * objects with ranks 1 – USERS_PER_PAGE.
     */
    @Test
    void onePage_FirstPage() {
        // Create the checklist for
        // USERS_PER_PAGE user objects.
        ArrayList<String> namesChecklist = getNamesChecklist(
                usersPerPage
        );


        // Custom presenter(s) for testing:
        LeaderboardOutputBoundary presenter = new LeaderboardOutputBoundary() {

            @Override
            public void changePagePrepareSuccessView(ChangePageOutputData results) {
                ArrayList<Object[]> userRankPairs = results.getUserRankPairs();

                // Sublist should have USERS_PER_PAGE pair elements.
                assertEquals(usersPerPage, userRankPairs.size());

                // User objects in the sublist should be the exact ones from memory.
                checkOffNames(userRankPairs, namesChecklist);

                // The user objects in the sublist should be exactly
                // those in memory, so the checklist should be empty.
                assertTrue(namesChecklist.isEmpty());

                // The ranks in the sublist in order should be
                // 1 – USERS_PER_PAGE.
                for (int i = 0; i < userRankPairs.size(); i++) {
                    assertEquals(i + 1,
                            userRankPairs.get(i)[1]);
                }
            }

            @Override
            public void changePagePrepareFailedView(String error) {
                fail("Use case failure is unexpected.");
            }
        };


        // Execute test(s).
        executeTest(presenter, 1);
    }

    /**
     * If there are more than USERS_PER_PAGE user
     * objects in memory, the result for page 1 should still
     * return a sublist with the first USERS_PER_PAGE pairs
     * of those user objects.
     */
    @Test
    void overOnePage_FirstPage() {
        // Create the checklist for
        // [(USERS_PER_PAGE*(rMultiple+1)]+rExtra user objects.
        // This ensures there is more than 1 page.
        ArrayList<String> namesChecklist = getNamesChecklist(
                usersPerPage * (rMultiple + 1) + rExtra
        );


        // Custom presenter(s) for testing:
        LeaderboardOutputBoundary presenter = new LeaderboardOutputBoundary() {

            @Override
            public void changePagePrepareSuccessView(ChangePageOutputData results) {
                ArrayList<Object[]> userRankPairs = results.getUserRankPairs();

                // Sublist should have USERS_PER_PAGE pair elements.
                assertEquals(usersPerPage, userRankPairs.size());

                // User objects in the sublist should be from memory.
                checkOffNames(userRankPairs, namesChecklist);

                // The user objects in the sublist should be exactly
                // the first USERS_PER_PAGE in memory, so the
                // checklist should have
                // (USERS_PER_PAGE*rMultiple)+rExtra elements left.
                assertEquals(usersPerPage * rMultiple + rExtra,
                        namesChecklist.size());

                // The ranks in the sublist in order should be
                // 1 – USERS_PER_PAGE.
                for (int i = 0; i < userRankPairs.size(); i++) {
                    assertEquals(i + 1,
                            userRankPairs.get(i)[1]);
                }
            }

            @Override
            public void changePagePrepareFailedView(String error) {
                fail("Use case failure is unexpected.");
            }
        };


        // Execute test(s).
        executeTest(presenter, 1);
    }

    /**
     * If there are USERS_PER_PAGE*rMultiple user
     * objects in memory, the result for subsequent pages should
     * return sublists with some USERS_PER_PAGE pairs of the
     * user objects in memory.
     * User objects from all pages should be exactly those in memory.
     */
    @Test
    void overOnePage_MorePagesExact() {
        // Create the checklist for
        // USERS_PER_PAGE*3 user objects.
        ArrayList<String> namesChecklist = getNamesChecklist(
                usersPerPage * 3
        );


        // Custom presenter(s) for testing:
        LeaderboardOutputBoundary presenterPage1 = new LeaderboardOutputBoundary() {

            @Override
            public void changePagePrepareSuccessView(ChangePageOutputData results) {
                ArrayList<Object[]> userRankPairs = results.getUserRankPairs();

                // resulting sublist should have USERS_PER_PAGE pair elements.
                assertEquals(usersPerPage, userRankPairs.size());

                // User objects in the sublist should be from memory.
                checkOffNames(userRankPairs, namesChecklist);

                // The ranks in the sublist in order should be
                // 1 – USERS_PER_PAGE.
                for (int i = 0; i < usersPerPage; i++) {
                    assertEquals(i + 1,
                            userRankPairs.get(i)[1]);
                }
            }

            @Override
            public void changePagePrepareFailedView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        LeaderboardOutputBoundary presenterPage2 = new LeaderboardOutputBoundary() {

            @Override
            public void changePagePrepareSuccessView(ChangePageOutputData results) {
                ArrayList<Object[]> userRankPairs = results.getUserRankPairs();

                // resulting sublist should have USERS_PER_PAGE pair elements.
                assertEquals(usersPerPage, userRankPairs.size());

                // User objects in the sublist should be from memory.
                checkOffNames(userRankPairs, namesChecklist);

                // The ranks in the sublist in order should be from
                // USERS_PER_PAGE+1 – USERS_PER_PAGE*2.
                for (int i = 0; i < usersPerPage; i++) {
                    assertEquals(i + usersPerPage + 1,
                            userRankPairs.get(i)[1]);
                }
            }

            @Override
            public void changePagePrepareFailedView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        LeaderboardOutputBoundary presenterPage3 = new LeaderboardOutputBoundary() {

            @Override
            public void changePagePrepareSuccessView(ChangePageOutputData results) {
                ArrayList<Object[]> userRankPairs = results.getUserRankPairs();

                // resulting sublist should have USERS_PER_PAGE pair elements.
                assertEquals(usersPerPage, userRankPairs.size());

                // User objects in the sublist should be from memory.
                checkOffNames(userRankPairs, namesChecklist);

                // The ranks in the sublist in order should be
                // (USERS_PER_PAGE*2)+1 – USERS_PER_PAGE*3.
                for (int i = 0; i < usersPerPage; i++) {assertEquals(
                            i + (2 * usersPerPage) + 1,
                            userRankPairs.get(i)[1]);
                }
            }

            @Override
            public void changePagePrepareFailedView(String error) {
                fail("Use case failure is unexpected.");
            }
        };


        // Execute test(s).
        executeTest(presenterPage1, 1);
        executeTest(presenterPage2, 2);
        executeTest(presenterPage3, 3);

        // The user objects from all sublists should be exactly
        // those in memory, so the checklist should be empty.
        assertTrue(namesChecklist.isEmpty());
    }

    /**
     * If there are (USERS_PER_PAGE*rMultiple)+rExtra user
     * objects in memory, the result for the last page
     * (page rMultiple+1) should return a sublist with the last
     * rExtra pairs of those user objects.
     */
    @Test
    void overOnePage_MorePagesAndExtra() {
        // Create the checklist for
        // (USERS_PER_PAGE*rMultiple)+rExtra user objects.
        ArrayList<String> namesChecklist = getNamesChecklist(
                usersPerPage * rMultiple + rExtra
        );


        // Custom presenter(s) for testing:
        LeaderboardOutputBoundary presenter = new LeaderboardOutputBoundary() {

            @Override
            public void changePagePrepareSuccessView(ChangePageOutputData results) {
                ArrayList<Object[]> userRankPairs = results.getUserRankPairs();

                // resulting sublist should have rExtra pair elements.
                assertEquals(rExtra, userRankPairs.size());

                // User objects in the sublist should be from memory.
                checkOffNames(userRankPairs, namesChecklist);

                // The user objects from the sublist of the last page
                // should be exactly the last c user objects in memory,
                // so the checklist have USERS_PER_PAGE*rMultiple elements left.
                assertEquals(usersPerPage * rMultiple,
                        namesChecklist.size());

                // The ranks in the sublist in order should be
                // USERS_PER_PAGE*(rMultiple-1) –
                // [USERS_PER_PAGE*(rMultiple-1)]+rExtra.
                for (int i = 0; i < rExtra; i++) {
                    assertEquals(i + usersPerPage * rMultiple + 1,
                            userRankPairs.get(i)[1]);
                }
            }

            @Override
            public void changePagePrepareFailedView(String error) {
                fail("Use case failure is unexpected.");
            }
        };


        // Execute test(s).
        executeTest(presenter, rMultiple + 1);
    }


    // Fail tests:

    /**
     * Presenter for fail tests.
     * Just need to know changePagePrepareFailedView runs.
     */
    LeaderboardOutputBoundary failPresenter = new LeaderboardOutputBoundary() {

        @Override
        public void changePagePrepareSuccessView(ChangePageOutputData results) {
            fail("Use case success is unexpected.");
        }

        @Override
        public void changePagePrepareFailedView(String error) {
            assertTrue(true);
        }
    };

    /**
     * Executing with page 0 will fail.
     */
    @Test
    void pageZero() {
        // Store any number of user objects in memory.
        for (int i = 0; i < rArbitrary; i++) {
            simulatedMemory.save(
                    factory.create("Sunny" + i, "password")
            );
        }

        // Execute test(s).
        executeTest(failPresenter, 0);
    }

    /**
     * Executing with a negative page will fail.
     */
    @Test
    void negativePage() {
        // Store any number of user objects in memory.
        for (int i = 0; i < rArbitrary; i++) {
            simulatedMemory.save(
                    factory.create("Sunny" + i, "password")
            );
        }

        // Execute test(s).
        executeTest(failPresenter, -rExtreme);
    }

    /**
     * If there are USERS_PER_PAGE*rMultiple user objects
     * in memory, then executing with page rMultiple+1 will fail.
     */
    @Test
    void overLastPage() {
        // Store USERS_PER_PAGE*rMultiple user objects in memory.
        for (int i = 0; i < usersPerPage * rMultiple; i++) {
            simulatedMemory.save(
                    factory.create("Sunny" + i, "password")
            );
        }

        // Execute test(s).
        executeTest(failPresenter, rMultiple + 1);
    }

    /**
     * If there are (USERS_PER_PAGE*rMultiple)+rExtra
     * user objects in memory , then executing with page
     * rMultiple+2 will fail.
     */
    @Test
    void overLastPageAndExtra() {
        // Store (USERS_PER_PAGE * rMultiple) + rExtra user objects in memory.
        for (int i = 0; i < usersPerPage * rMultiple + rExtra; i++) {
            simulatedMemory.save(
                    factory.create("Sunny" + i, "password")
            );
        }

        // Execute test(s).
        executeTest(failPresenter, rMultiple + 2);
    }

    /**
     * If there are (USERS_PER_PAGE*rMultiple)+rExtra
     * user objects in memory , then executing with page more
     * than rMultiple+2 will fail.
     */
    @Test
    void veryOverLastPage() {
        // Store (USERS_PER_PAGE * rMultiple) + rExtra user objects in memory.
        for (int i = 0; i < usersPerPage * rMultiple + rExtra; i++) {
            simulatedMemory.save(
                    factory.create("Sunny" + i, "password")
            );
        }

        // Execute test(s).
        executeTest(failPresenter, rMultiple + 2 + rExtreme);
    }

}
