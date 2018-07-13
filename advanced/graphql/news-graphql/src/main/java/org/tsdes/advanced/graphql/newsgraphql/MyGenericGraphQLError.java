package org.tsdes.advanced.graphql.newsgraphql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.List;

/*
    TODO remove once Kotlin fixed

    Also check
    https://github.com/graphql-java/graphql-java/issues/1123
 */

public class MyGenericGraphQLError extends RuntimeException implements GraphQLError {


    public MyGenericGraphQLError(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    @JsonIgnore
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    @JsonIgnore
    public ErrorType getErrorType() {
        return null;
    }
}
