package com.custom.domain.exception

class LocationDisabledException() : Exception("Location services are disabled.")
class LocationCanceledException() : Exception("Location request has been canceled.")