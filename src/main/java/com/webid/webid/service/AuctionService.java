import java.util.ArrayList;
import java.util.Optional;

import com.webid.webid.model.Auction;
import com.webid.webid.model.User;
import com.webid.webid.repository.AuctionRepository;

public class AuctionService {

    private final AuctionRepository ar;

    public AuctionService(AuctionRepository ar) {
        this.ar = ar;
    }

    public ArrayList<Auction> search(String itemName) {
        return this.ar.findAuctionByItemName(itemName);
    }

    public Auction select(ArrayList<Auction> items, Long id) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == id) {
                return items.get(i);
            }
        }
        return null;
    }

    // for Forward auctions
    public boolean setNewBid(Auction foundAuction, double bidAmount, User user) {
        // find auction first; if item is not found immediately already returns false
        if (foundAuction == null) {
            return false;
        } else { // auction is real; server processes bid amount and new current bidder
            if (bidAmount > foundAuction.getCurrentBid()) {
                foundAuction.setCurrentBid(bidAmount);
                foundAuction.setCurrentBidderID(user.getId());

                // notifies subscribed users

                return true;
            } else {
                // return bid too small exception
                return false; // for now just return false
            }
        }
    }

    // For Dutch auctions
    public boolean confirmBid(Auction foundAuction, User user) {
        if (foundAuction == null) {
            return false;
        } else { // auction is found; server processes purchase
            foundAuction.setCurrentBidderID(user.getId());
            foundAuction.completeAuction();
        }
    }
}
