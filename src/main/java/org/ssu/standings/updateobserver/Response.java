package org.ssu.standings.updateobserver;

public class Response {
    private Long lastModified;
    private String content;

    private Response(Builder builder) {
        this.content = builder.content;
        this.lastModified = builder.lastModified;
    }

    public String getContent() {
        return content;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public static final class Builder {
        private Long lastModified;
        private String content;

        public Builder withLastModified(Long lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public Builder withContent(String content) {
            this.content = content;
            return this;
        }

        public Response build() {
            return new Response(this);
        }
    }
}
