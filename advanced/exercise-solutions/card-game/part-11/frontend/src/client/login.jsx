import React from "react";
import { Link, withRouter } from "react-router-dom";

export class Login extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      userId: "",
      password: "",
      errorMsg: null,
    };
  }

  onUserIdChange = (event) => {
    this.setState({ userId: event.target.value });
  };

  onPasswordChange = (event) => {
    this.setState({ password: event.target.value });
  };

  doLogIn = async () => {
    const { userId, password } = this.state;

    const url = "/api/auth/login";

    const payload = { userId: userId, password: password };

    let response;

    try {
      response = await fetch(url, {
        method: "post",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      });
    } catch (err) {
      this.setState({ errorMsg: "Failed to connect to server: " + err });
      return;
    }

    if (response.status === 401) {
      this.setState({ errorMsg: "Invalid userId/password" });
      return;
    }

    if (response.status !== 204) {
      this.setState({
        errorMsg:
          "Error when connecting to server: status code " + response.status,
      });
      return;
    }

    this.setState({ errorMsg: null });
    await this.props.fetchAndUpdateUserInfo();
    this.props.history.push("/");
  };

  render() {
    let error = <div></div>;
    if (this.state.errorMsg) {
      error = (
        <div className="errorMsg">
          <p>{this.state.errorMsg}</p>
        </div>
      );
    }

    return (
      <div className="center">
        <div>
          <p>User Id:</p>
          <input
            type="text"
            value={this.state.userId}
            onChange={this.onUserIdChange}
            id="userIdInput"
          />
        </div>
        <div>
          <p>Password:</p>
          <input
            type="password"
            value={this.state.password}
            onChange={this.onPasswordChange}
            id="passwordInput"
          />
        </div>

        {error}
        <div>
          <button className="button" onClick={this.doLogIn} id="loginBtn">
            Log In
          </button>
        </div>
        <Link className="button" to={"/signup"}>
          Register
        </Link>
      </div>
    );
  }
}

export default withRouter(Login);
