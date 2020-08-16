import React from "react";
import {withRouter} from "react-router-dom";

export class Collection extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            userStats: null,
            collection: null
        }
    }

    componentDidMount() {
        this.fetchUserStats()
    }

    fetchUserStats = async () => {

        const userId = this.props.userId;
        const url = "/api/user-collections/" + userId;

        let response;

        //TODO remove once fixed with AMQP
        await fetch(url, {method: "put"})

        try {
            response = await fetch(url, {method: "get"});
        } catch (err) {
            this.setState({errorMsg: "Failed to connect to server: " + err});
            return;
        }

        if (response.status === 401) {
            this.props.updateLoggedInUser(null);
            this.props.history.push("/");
            return;
        }

        if (response.status === 404) {
            this.setState({errorMsg: "Error: user info not available for " + userId});
            return;
        }

        if (response.status !== 200) {
            this.setState({
                errorMsg: "Failed connection to server. Status " + response.status,
            });
            return;
        }

        const stats = await response.json();
        this.setState({userStats: stats.data, errorMsg: null});
    };

    render() {

        if(!this.state.userStats){
            return (<p>Loading user collection...</p>);
        }

        return (
            <div>
                <p>Coins: {this.state.userStats.coins}</p>
                <p>Packs: {this.state.userStats.cardPacks}</p>
            </div>
        );
    }
}

export default withRouter(Collection);