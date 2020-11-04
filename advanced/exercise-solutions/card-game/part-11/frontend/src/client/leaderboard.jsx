import React from "react";
import {withRouter} from "react-router-dom";

export class Leaderboard extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            scores: [],
            next: null,
            error: null
        };
    }


    componentDidMount() {
        this.fetchScores();
    }

    fetchScores = async () => {

        let url = "/api/scores";
        if(this.state.next !== null){
            url = this.state.next;
        }

        let response;
        let payload;

        try {
            response = await fetch(url);
            payload = await response.json();
        } catch (err) {
            //Network error: eg, wrong URL, no internet, etc.
            this.setState({
                error: "ERROR when retrieving scores: " + err,
                scores: [],
                next: null
            });
            return;
        }

        if (response.status === 200) {
            this.setState(prev => {
                return {
                    error: null,
                    scores: [...prev.scores,...payload.data.list],
                    next: payload.data.next
                }
            });
        } else {
            this.setState({
                error: "Issue with HTTP connection: status code " + response.status
                    + ". " + payload.error,
                scores: [],
                next: null
            });
        }
    };


    render() {

        let table;

        if (this.state.error !== null) {
            table = <p>{this.state.error}</p>
        } else if (!this.state.scores || this.state.scores.length === 0) {
            table = <p>There is no leaderboard info</p>
        } else {
            table = 
            <div>
                <table className="allScores">
                    <thead>
                    <tr>
                        <th>Player</th>
                        <th>Victories/Draws/Defeats</th>
                        <th>Score</th>
                    </tr>
                    </thead>
                    <tbody className="leaderboard-rows">
                    {this.state.scores.map(s =>
                        <tr key={"key_" + s.id} className="leaderboard-row" >
                            <td>{s.userId}</td>
                            <td>{s.victories+"/"+s.draws+"/"+s.defeats}</td>
                            <td>{s.score}</td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>
        }

        return (
            <div className="center">
                <h2>LeaderBoard</h2>
                {table}
                {this.state.next &&
                <button className="button" onClick={this.fetchScores}>Next</button>
                }
            </div>
        );
    }
}

export default withRouter(Leaderboard);