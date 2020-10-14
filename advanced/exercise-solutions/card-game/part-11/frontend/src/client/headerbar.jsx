import React from "react";
import { Link, withRouter } from "react-router-dom";

/*
    Just provide a header component for all pages, where we have a link to the
    home-page, and login/signup/logout buttons.
 */
export class HeaderBar extends React.Component {
  constructor(props) {
    super(props);
  }

  doLogout = async () => {
    const url = "/api/auth/logout";

    let response;

    try {
      response = await fetch(url, { method: "post" });
    } catch (err) {
      alert("Failed to connect to server: " + err);
      return;
    }

    if (response.status !== 204) {
      alert("Error when connecting to server: status code " + response.status);
      return;
    }

    this.props.updateLoggedInUser(null);
    this.props.history.push("/");
  };

  renderLoggedIn(userId) {
    return (
      <React.Fragment>
        <p className="header-text">
          Welcome {userId}!
        </p>
        <Link className="header-link" to="/collection" tabIndex="0">My Collection</Link>
        <Link className="header-link" to="/play" tabIndex="0">Play</Link>
        <Link className="header-link" to="/leaderboard" tabIndex="0">Leaderboard</Link>
        <button className="header-link header-link-logout" onClick={this.doLogout} id="logoutBtnId">Logout</button>
      </React.Fragment>
    );
  }

  renderNotLoggedIn() {
    return (
      <React.Fragment>
        <p className="header-text">You are not logged in</p>
        <div className="action-buttons">
          <Link className="header-link" to="/login" tabIndex="0">LogIn</Link>
          <Link className="header-link" to="/signup" tabIndex="0">SignUp</Link>
        </div>
      </React.Fragment>
    );
  }

  render() {
    const userId = this.props.userId;

    let content;
    if (!userId) {
      content = this.renderNotLoggedIn();
    } else {
      content = this.renderLoggedIn(userId);
    }

    return (
      <div className="header">
        <Link className="header-logo" to={"/"} tabIndex="0">
          Card Game
        </Link>
        {content}
      </div>
    );
  }
}

export default withRouter(HeaderBar);
