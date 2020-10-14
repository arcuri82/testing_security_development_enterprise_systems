import React from "react";
import { withRouter } from "react-router-dom";

export class Home extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        return(
          <p className="home-welcome">
              Welcome to our Collectible Card Game!!!
          </p>
        );
    }
}

export default withRouter(Home);