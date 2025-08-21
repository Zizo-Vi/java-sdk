package io.modelcontextprotocol.client;

import io.modelcontextprotocol.client.auth.HttpClientAuthenticator;
import io.modelcontextprotocol.client.auth.OAuthClientProvider;
import io.modelcontextprotocol.client.transport.AuthenticatedTransportBuilder;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.client.transport.HttpClientStreamableHttpTransport;
import io.modelcontextprotocol.client.transport.RequestResponseInterceptor;
import io.modelcontextprotocol.spec.McpSchema;

import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Factory for creating MCP clients with authentication.
 */
public class McpClientFactory {

	/**
	 * Creates a new MCP client with authentication.
	 * @param serverUrl The server URL
	 * @param authProvider The OAuth client provider
	 * @return The MCP client
	 */
	public static McpAsyncClient createAuthenticatedAsyncClient(String serverUrl, OAuthClientProvider authProvider,
			String name, String endpoint, Duration requestTimeout) {
		// Create transport with authentication
		HttpClientStreamableHttpTransport transport = AuthenticatedTransportBuilder
			.withAuthentication(HttpClientStreamableHttpTransport.builder(serverUrl).endpoint(endpoint), authProvider)
			.build();

		// Create MCP client
		if (name == null || name.trim().equals("")) {
			name = "Spring AI MCP Client";
		}
		McpSchema.Implementation clientInfo = new McpSchema.Implementation(name, "0.3.1");
		return McpClient.async(transport).clientInfo(clientInfo).requestTimeout(requestTimeout).build();
	}

	/**
	 * Creates a new MCP client with authentication.
	 * @param serverUrl The server URL
	 * @param authProvider The OAuth client provider
	 * @return The MCP client
	 */
	public static McpSyncClient createAuthenticatedSyncClient(String serverUrl, OAuthClientProvider authProvider,
			String name) {
		// Create transport with authentication
		HttpClientSseClientTransport transport = AuthenticatedTransportBuilder
			.withAuthentication(HttpClientSseClientTransport.builder(serverUrl).sseEndpoint("/sse"), authProvider)
			.build();

		// Create MCP client
		if (name == null || name.trim().equals("")) {
			name = "Spring AI MCP Client";
		}
		McpSchema.Implementation clientInfo = new McpSchema.Implementation(name, "0.3.1");
		return McpClient.sync(transport).clientInfo(clientInfo).build();
	}

}