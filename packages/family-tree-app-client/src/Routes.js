import React from "react";
import { Route, Switch } from "react-router-dom";
import Home from "./containers/Home";
import NotFound from "./containers/NotFound";
import Login from "./containers/Login";
import FamilyTable from "./containers/CurrenTime";
import AuthenticatedRoute from "./components/AuthenticatedRoute";
import UnauthenticatedRoute from "./components/UnauthenticatedRoute";
import NewPassword from "./containers/NewPassword";
import SignUp from "./containers/SignUp";
import ListPersons from "./containers/ListPersons";
import AddPerson from "./containers/AddPerson";

export default function Routes({ appProps }) {
  return (
    <Switch>
      <UnauthenticatedRoute path="/" exact component={Home} appProps={appProps} />
      <UnauthenticatedRoute path="/login" exact component={Login} appProps={appProps} />
      <UnauthenticatedRoute path="/new-password" exact component={NewPassword} appProps={appProps} />
      <UnauthenticatedRoute path="/sign-up" exact component={SignUp} appProps={appProps} />
      <AuthenticatedRoute path="/current-time" exact component={FamilyTable} appProps={appProps} />
      <AuthenticatedRoute path="/persons" exact component={ListPersons} appProps={appProps} />
      <AuthenticatedRoute path="/add-person" exact component={AddPerson} appProps={appProps} />
      { /* Finally, catch all unmatched routes */}
      <Route component={NotFound} />
    </Switch>
  );
}