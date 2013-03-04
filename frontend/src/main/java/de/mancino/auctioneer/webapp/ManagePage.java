package de.mancino.auctioneer.webapp;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.mancino.auctioneer.bo.ArmoryCharacterBO;
import de.mancino.auctioneer.dao.ArmoryCharacterDAO;
import de.mancino.auctioneer.dao.ArmoryItemDAO;
import de.mancino.auctioneer.dao.AuctioneerDatabaseDAO;
import de.mancino.auctioneer.dao.CashSampleDAO;
import de.mancino.auctioneer.dao.PriceSampleDAO;
import de.mancino.auctioneer.dao.PriceWatchDAO;
import de.mancino.auctioneer.dto.ArmoryCharacter;
import de.mancino.utils.Tuple;

@AuthorizeInstantiation(Roles.ADMIN)
public class ManagePage extends BasePage {

    @SpringBean
    private PriceWatchDAO priceWatchDAO;
    @SpringBean
    private ArmoryItemDAO armoryItemDAO;
    @SpringBean
    private CashSampleDAO cashSampleDAO;
    @SpringBean
    private ArmoryCharacterDAO armoryCharacterDAO;
    @SpringBean
    private ArmoryCharacterBO armoryCharacterBO;
    @SpringBean
    private PriceSampleDAO priceSampleDAO;
    @SpringBean
    private AuctioneerDatabaseDAO auctioneerDatabaseDAO;

    public ManagePage() {
        long totalGold = 0;
        long totalSilver = 0;
        // !!! ACHTUNG !!!
        // toLong() geht nicht! als Int wären maximal 214.748 Gold ( -> (2^31)/100/100) darstellbar!
        for(ArmoryCharacter character : armoryCharacterBO.listArmoryCharacters()) {
            totalGold += character.getCurrentCash().getGold();
            totalSilver += character.getCurrentCash().getSilver();
        }
        totalGold += totalSilver / 100;
        totalSilver = totalSilver % 100;
        final String goldAmount = String.valueOf(totalGold + " Gold " + totalSilver + " Silber");

        add(new Label("goldAmount", goldAmount));
        add(new Label("itemCount", String.valueOf(armoryItemDAO.getSize())));
        add(new Label("priceWatchCount", String.valueOf(priceWatchDAO.getSize(true))));
        add(new Label("priceSampleCount", String.valueOf(priceSampleDAO.getSize())));
        add(new Label("characterCount", String.valueOf(armoryCharacterDAO.getSize())));
        add(new Label("cashSampleCount", String.valueOf(cashSampleDAO.getSize())));

        LoadableDetachableModel<List<Tuple<String,String>>> memoryUsageModel = new LoadableDetachableModel<List<Tuple<String,String>>>() {
            private static final long serialVersionUID = 1L;
            private static final long MB = 1024*1024;
            @Override
            protected List<Tuple<String,String>> load() {
                List<Tuple<String,String>> memoryUsage = new ArrayList<Tuple<String,String>>();
                Iterator<MemoryPoolMXBean> iter = ManagementFactory.getMemoryPoolMXBeans().iterator();
                while (iter.hasNext()) {
                    final MemoryPoolMXBean item = (MemoryPoolMXBean) iter.next();
                    Tuple<String, String> usage = new Tuple<String, String>(
                            item.getName(), 
                            toMB(item.getUsage().getUsed()) + "/"+  toMB(item.getUsage().getMax()) + " MB");
                    memoryUsage.add(usage);
                }
                memoryUsage.add(new Tuple<String,String>("MySQL Database", toMB(auctioneerDatabaseDAO.getDatabaseSize()) + " MB"));
                return memoryUsage;
            }

            private String toMB(long bytes) {
                final long mb = bytes/MB;
                return bytes%MB>0 ? String.valueOf(mb+1) : String.valueOf(mb);
            }
        };
        add(new ListView<Tuple<String,String>>("memoryUsage", memoryUsageModel) {
            private static final long serialVersionUID = 1L;
            @Override
            protected void populateItem(ListItem<Tuple<String,String>> item) {
                item.add(new Label("name", item.getModelObject().x));
                item.add(new Label("usage", item.getModelObject().y));
            }
        });
    }

}
