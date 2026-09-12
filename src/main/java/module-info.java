module org.conta {
    requires org.apache.httpcomponents.httpclient.fluent;
    requires org.apache.httpcomponents.httpclient;
    requires com.google.gson;
    opens conta.domain.model to com.google.gson;
    exports conta.domain.model;
    exports conta.api;
}