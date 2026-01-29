package interface_adapter.get_topItems;
import interface_adapter.ViewModel;

/**
 * The ViewModel for the GetTopSongs View.
 */

public class GetTopItemsViewModel extends ViewModel<GetTopItemsState> {

    public GetTopItemsViewModel() {
        super("top Items");
        setState(new GetTopItemsState());
    }
}
