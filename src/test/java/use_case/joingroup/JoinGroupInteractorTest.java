package use_case.joingroup;
import entity.Group;
import entity.SpotifyUser;
import use_case.create_group.GroupDataAccessInterface;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


public class JoinGroupInteractorTest {
    public void testJoinGroupSuccess() {
        GroupDataAccessInterface mockDao = mock(GroupDataAccessInterface.class);
        SpotifyUser newUser = new SpotifyUser("newUser", "newaccesstoken", "newrefreshtoken","newID" );
        Group group = new Group("FunGroup");
        when(mockDao.getGroupByCode("FunGroup")).thenReturn(group);
        JoinGroupOutputBoundary presenter = new JoinGroupOutputBoundary() {
            @Override
            public void prepareSuccessView(JoinGroupOutputData outputData) {
                assertTrue(outputData.isSuccess());
                assertEquals("FunGroup", outputData.getGroupName());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Should not fail when joining group successfully");
            }
        };
        JoinGroupInteractor interactor = new JoinGroupInteractor(mockDao, presenter);
        JoinGroupInputData inputData = new JoinGroupInputData("FunGroup", newUser);
        interactor.joinGroup(inputData);
        verify(mockDao).updateGroup(group);
        assertTrue(group.getUsers().contains(newUser));
    }

    }
