/**
 * source: https://github.com/AnomalyInnovations/serverless-stack-demo-client/blob/master/src/components/AuthenticatedRoute.js
 */
import React from "react";
import { Route, Redirect } from "react-router-dom";

export default function AuthenticatedRoute({ component: C, appProps, ...rest }) {
  return (
    <Route
      {...rest}
      render={props =>
        appProps.isAuthenticated
          ? <C {...props} {...appProps} />
          : <Redirect
              to={`/login?redirect=${props.location.pathname}${props.location
                .search}`}
            />}
    />
  );
}