import React from "react";
import ReactDOM from "react-dom";
import {BrowserRouter, Route, Switch} from "react-router-dom";

import Home from "./home";
import Play from "./play";
import Login from "./login";
import SignUp from "./signup";
import HeaderBar from "./headerbar";
import Collection from "./collection";
import Leaderboard from "./leaderboard"

class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            user: null,
        };
    }

    componentDidMount() {
        this.fetchAndUpdateUserInfo();
    }

    componentWillUnmount() {
    }

    fetchAndUpdateUserInfo = async () => {
        const url = "/api/auth/user";

        let response;

        try {
            response = await fetch(url, {
                method: "get",
            });
        } catch (err) {
            this.setState({errorMsg: "Failed to connect to server: " + err});
            return;
        }

        if (response.status === 401) {
            //that is ok
            this.updateLoggedInUser(null);
            return;
        }

        if (response.status !== 200) {
            //TODO here could have some warning message in the page.
        } else {
            const payload = await response.json();
            this.updateLoggedInUser(payload);
        }
    };

    updateLoggedInUser = (user) => {
        this.setState({user: user});
    };

    notFound() {
        return (
            <div>
                <h2>NOT FOUND: 404</h2>
                <p>ERROR: the page you requested in not available.</p>
            </div>
        );
    }

    render() {

        const id = this.state.user ? this.state.user.name : null;

        return (
            <BrowserRouter>
                <div>
                    <HeaderBar userId={id} updateLoggedInUser={this.updateLoggedInUser}/>
                    <Switch>
                        <Route exact path="/login" render={(props) => (
                            <Login{...props}
                                  fetchAndUpdateUserInfo={this.fetchAndUpdateUserInfo}
                            />
                        )}/>
                        <Route exact path="/signup" render={(props) => (
                            <SignUp {...props} fetchAndUpdateUserInfo={this.fetchAndUpdateUserInfo}/>
                        )}/>
                        <Route exact path={"/play"}><Play/></Route>
                        <Route exact path="/" render={(props) => (
                            <Home{...props}
                                 user={this.state.user}
                                 fetchAndUpdateUserInfo={this.fetchAndUpdateUserInfo}
                            />
                        )}/>
                        <Route exact path="/leaderboard" render={(props) => (
                            <Leaderboard{...props} fetchAndUpdateUserInfo={this.fetchAndUpdateUserInfo}/>
                        )}/>
                        <Route exact path="/collection" render={(props) => (
                            <Collection{...props} userId={id} fetchAndUpdateUserInfo={this.fetchAndUpdateUserInfo}/>
                        )}/>
                        <Route component={this.notFound}/>
                    </Switch>
                </div>
            </BrowserRouter>
        );
    }
}

ReactDOM.render(<App/>, document.getElementById("root"));
