package at.medunigraz.imi.bst.trec.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.net.Socket;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.junit.Assume;
import org.junit.Test;

import at.medunigraz.imi.bst.config.TrecConfig;

public class ElasticClientFactoryTest {

	public ElasticClientFactoryTest() {
		// There must be an available server
		Assume.assumeTrue(checkOpenPort(TrecConfig.ELASTIC_HOSTNAME, TrecConfig.ELASTIC_PORT));
	}

	@Test
	public void testGetClient() {
		Client client = ElasticClientFactory.getClient();

		ClusterHealthResponse health = client.admin().cluster().prepareHealth().get();
		String actual = health.getClusterName();
		assertEquals("elasticsearch", actual); // Any check
	}

	@Test
	public void testHealth() {
		Client client = ElasticClientFactory.getClient();

		ClusterHealthResponse health = client.admin().cluster().prepareHealth().get();
		ClusterHealthStatus status = health.getStatus();
		int actual = status.compareTo(ClusterHealthStatus.RED);
		assertNotEquals(0, actual); // 0 = RED
	}

	/**
	 * Checks whether a given port is open on a server
	 * 
	 * @param hostname
	 * @param port
	 * @return
	 */
	private boolean checkOpenPort(String hostname, int port) {
		try (Socket ignored = new Socket(hostname, port)) {
			return true;
		} catch (IOException ignored) {
			return false;
		}
	}
}
