package org.tsdes.advanced.rest.wrapper

import org.tsdes.advanced.rest.dto.WrappedResponse

/*
    Note that we could directly instantiate WrappedResponse<DivisionDto>.
    However, then we end up with a class with Generics, which might complicate
    in some cases the use of libraries for marshalling JSON.
    A workaround is to just have a concrete subclass bound to that Generic value
 */
class ResponseDto(
        code: Int? = null,
        data: DivisionDto? = null,
        message: String? = null,
        status: ResponseStatus? = null

) : WrappedResponse<DivisionDto>(code, data, message, status)