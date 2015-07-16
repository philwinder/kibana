package org.apache.mesos.kibana.scheduler;

import junit.framework.TestCase;
import org.apache.mesos.Protos;
import org.apache.mesos.SchedulerDriver;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class KibanaSchedulerTest extends TestCase {

    //TODO Move to a TestUtils class or something...
    private static List<Protos.Offer> getValidOffers(int amount) {
        List<Protos.Offer> offers = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Protos.Offer offer = Protos.Offer.newBuilder()
                    .setId(Protos.OfferID.newBuilder().setValue("offer-" + i).build())
                    .setSlaveId(Protos.SlaveID.newBuilder().setValue("slave-" + i).build())
                    .setFrameworkId(Protos.FrameworkID.newBuilder().setValue("KibanaFramework").build())
                    .setHostname("localhost")
                    .addResources(ResourceHelper.cpus(Configuration.getCPU()))
                    .addResources(ResourceHelper.mem(Configuration.getMEM()))
                    .addResources(ResourceHelper.ports(5601L, (long) (5600L + Configuration.getPORTS())))
                    .build();
            offers.add(offer);
        }
        return offers;
    }

    @Test
    public void testResourceOffers_startsSingleInstanceForSingleElasticSearch() throws Exception {
        String elasticSearch1 = "myElasticSearch1:9200";
        Configuration configuration = new Configuration();
        KibanaScheduler scheduler = new KibanaScheduler(configuration);
        SchedulerDriver driver = mock(SchedulerDriver.class);

        configuration.putRequiredInstances(elasticSearch1, 1);

        scheduler.resourceOffers(driver, getValidOffers(1));

        assertTrue(configuration.runningInstances.containsKey(elasticSearch1));
        assertEquals(1, configuration.runningInstances.get(elasticSearch1).size());
    }

    @Test
    public void testResourceOffers_startsMultipleInstancesForSingleElasticSearch() throws Exception {
        String elasticSearch1 = "myElasticSearch1:9200";
        Configuration configuration = new Configuration();
        KibanaScheduler scheduler = new KibanaScheduler(configuration);
        SchedulerDriver driver = mock(SchedulerDriver.class);

        configuration.putRequiredInstances(elasticSearch1, 3);

        scheduler.resourceOffers(driver, getValidOffers(3));

        assertTrue(configuration.runningInstances.containsKey(elasticSearch1));
        assertEquals(3, configuration.runningInstances.get(elasticSearch1).size());
    }
}