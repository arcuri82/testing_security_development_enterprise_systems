package org.tsdes.advanced.rest.wrapper

import org.tsdes.advanced.rest.dto.WrappedResponse


class IntResponseDto(
        code: Int? = null,
        data: Int? = null,
        message: String? = null,
        status: ResponseStatus? = null

) : WrappedResponse<Int>(code, data, message, status)