package com.bit.sfa.model;


import org.json.JSONException;
import org.json.JSONObject;

public class Route {
    private String routeCode,routeName;

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public static Route parseRoute(JSONObject instance) throws JSONException {

        if (instance != null) {
            Route route = new Route();
            route.setRouteCode(instance.getString("routecode"));
            route.setRouteName(instance.getString("routename"));

            return route;
        }

        return null;
    }
}
