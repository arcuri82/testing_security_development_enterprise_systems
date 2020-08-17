import React from "react";
import {withRouter} from "react-router-dom";
import Card from "./card"

export class Collection extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            userStats: null,
            collection: null,
            openedPack: null
        }
    }

    componentDidMount() {
        this.fetchCollection()
        this.fetchUserStats()
    }

    fetchCollection = async () => {

        const url = "/api/cards/collection_v1_000" ;

        let response;

        try {
            response = await fetch(url, {method: "get"});
        } catch (err) {
            this.setState({errorMsg: "Failed to connect to server: " + err});
            return;
        }

        if (response.status !== 200) {
            this.setState({
                errorMsg: "Failed connection to server. Status " + response.status,
            });
            return;
        }

        const payload = await response.json();
        this.setState({collection: payload.data, errorMsg: null});
    };


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

        const payload = await response.json();
        this.setState({userStats: payload.data, errorMsg: null});
    };


    openPack = async () =>{
        const userId = this.props.userId;
        const url = "/api/user-collections/" + userId;

        let response;

        const data = {
            command: "OPEN_PACK"
        }

        try {
            response = await fetch(url, {
                method: "PATCH",
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(data)
            });
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
            this.setState({errorMsg: "Error: info not available for " + userId});
            return;
        }

        const payload = await response.json();

        if (response.status !== 200) {
            this.setState({
                errorMsg: "Failure. Status " + response.status +". Msg: " + payload.message
            });
            return;
        }

        this.setState({openedPack: payload.data, errorMsg: null});
        this.fetchUserStats()
    }

    closePackView =  () =>{
        this.setState({openedPack: null})
    }

    render() {

        if(this.state.errorMsg){
            return (<p>{this.state.errorMsg}</p>)
        }

        if(!this.state.userStats){
            return (<p>Loading user collection...</p>);
        }
        if(!this.state.collection){
            return (<p>Loading cards...</p>)
        }

        if(this.state.openedPack){
            const cards = this.state.openedPack.cardIdsInOpenedPack.map(id => {
                 return this.state.collection.cards.find(c => c.cardId === id)
            });

            return (
                <div>
                    <h1>Pack Content</h1>
                    {cards.map(c => <Card key={c.cardId} {...c}/>)}
                    <button onClick={this.closePackView}>Close</button>
                </div>
            );
        }

        const packs = this.state.userStats.cardPacks

        return (
            <div>
                <p>Coins: {this.state.userStats.coins}</p>
                <p>Packs: {packs}</p>

                <button disabled={packs<=0} onClick={this.openPack}>Open Pack</button>

                {this.state.collection.cards.map(c => {
                    const info = this.state.userStats.ownedCards.find(z => z.cardId===c.cardId)
                    const quantity = info ? info.numberOfCopies : 0
                     return  <Card key={c.cardId} {...c} quantity={quantity}/>
                    }
                    )}
            </div>
        );
    }
}

export default withRouter(Collection);