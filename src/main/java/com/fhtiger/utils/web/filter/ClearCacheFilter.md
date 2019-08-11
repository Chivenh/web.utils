###ClearCacheFilter 配置如下:
    <filter>
    <filter-name>ClearCacheFilter</filter-name>
    <filter-class>com.fhtiger.utils.web.filter.ClearCacheFilter</filter-class>
        <init-param>
        <param-name>exurl</param-name>
          <!-- 此处配置匹配的文件正则表达式,多个用`,`隔开-->
          <param-value>
          /static/(lib|tpl|json)/.*,
          /static/js/directives/.*,
          /static/css/(([^/]*)|(themes/.*))\.css
          </param-value>
        </init-param>
      </filter>
    
    <filter-mapping>
        <filter-name>ClearCacheFilter</filter-name>
        <url-pattern>*.js</url-pattern>
        <url-pattern>*.css</url-pattern>
        <url-pattern>*.html</url-pattern>
    </filter-mapping>
  