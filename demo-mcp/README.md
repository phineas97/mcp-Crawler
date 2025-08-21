# MCP使用流程

### 1. 安装并启动MCP

``` npx @playwright/mcp@latest --port 8931 ```

npx: Node.js 的包执行器，用于执行 npm 仓库中的包。

@playwright/mcp@latest: 启动一个基于 Playwright 的 MCP 服务器。

--port 8931: 这个参数告诉 playwright MCP 服务器，它应该在本地的 8931 端口上监听请求。

所以，这个命令在本地机器上启动一个 Playwright 服务，并使其作为一个 MCP 服务器，监听来自客户端的连接

其入口点在 ``` http://localhost:8931 ```

### 2. 启动主程序

读取``` application.properties ```文件中的配置：

``` spring.ai.mcp.client.sse.connections.playwright.url=http://localhost:8931/sse ```

这个配置告诉 Spring Boot 应用作为 MCP 客户端，需要与一个 MCP 服务器进行通信。

连接类型：使用 SSE (Server-Sent Events) 连接。

服务器地址：这个服务器的地址是``` http://localhost:8931/sse ```

### 3. 读取mcp-server-config.json

配置了MCP 服务器上可用的工具：fetch

LLM在处理提示时会识别到需要从 URL 获取内容的意图，并调用工具完成工作。



# Spring AI 框架设计：

McpSyncClient：这是最底层的客户端，它负责与高德的 mcp.amap.com 服务进行实际的 HTTP/SSE 通信。它有一个方法叫 listTools()，可以动态地从服务中获取所有可用的工具。

SyncMcpToolCallbackProvider：这个类是中间层，它实现了 ToolCallbackProvider 接口。它的作用就是作为 ChatClient 和 McpSyncClient 之间的桥梁。

它知道如何调用 McpSyncClient.listTools() 来获取工具列表。

它也知道如何根据 ChatClient 的请求，调用 McpSyncClient 的 callTool() 方法来执行具体的工具。

ChatClient：这是最高层的聊天接口。它不需要直接知道所有工具的细节，它只需要一个“工具提供者”（ToolCallbackProvider）。