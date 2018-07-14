package org.tsdes.advanced.rest.wrapper

import org.tsdes.advanced.rest.dto.WrappedResponse


class ResponseDto(
        code: Int? = null,
        data: DivisionDto? = null,
        message: String? = null,
        status: ResponseStatus? = null

) : WrappedResponse<DivisionDto>(code, data, message, status)