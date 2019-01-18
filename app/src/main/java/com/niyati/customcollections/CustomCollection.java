//getters setters class for custom collections

package com.niyati.customcollections;

class CustomCollection {

    private String id;
    private String handle;
    private String title;
    private String updated_at;
    private String body_html;
    private String published_at;
    private String sort_order;
    private String template_suffix;
    private String published_scope;
    private String admin_graphql_api_id;
    private ImageClass image ;

    public ImageClass getImage() {
        return image;
    }
    public String getId() {
        return id;
    }

    public String getHandle() {
        return handle;
    }

    public String getTitle() {
        return title;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getBody_html() {
        return body_html;
    }

    public String getPublished_at() {
        return published_at;
    }

    public String getSort_order() {
        return sort_order;
    }

    public String getTemplate_suffix() {
        return template_suffix;
    }

    public String getPublished_scope() {
        return published_scope;
    }

    public String getAdmin_graphql_api_id() {
        return admin_graphql_api_id;
    }
}
