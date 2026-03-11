{#if config?.enabled && config.displayMode === 'SIDEBAR'}
  <div class="rounded-3 overflow-hidden position-relative" style="{containerStyle}">

    {#if config.backgroundImage}
      <div
        class="position-absolute top-0 start-0 w-100 h-100"
        style="background-image: url(/api/countdown-timer/background/{config.backgroundImage}); background-size: cover; background-position: center; z-index: 0; {config.theme === 'BLUR' ? 'filter: blur(6px); transform: scale(1.1);' : ''}">
      </div>
      <div
        class="position-absolute top-0 start-0 w-100 h-100"
        style="{overlayStyle} z-index: 1;">
      </div>
    {/if}

    <div class="text-center p-3 position-relative" style="z-index: 2; color: {config.textColor || '#ffffff'};">
      {#if config.showLogo && siteInfo}
        <div class="mb-2">
          <img
            src="/api/websiteLogo?hash={siteInfo.websiteLogoHash}"
            alt={siteInfo.websiteName}
            style="max-height: {logoHeight}px; max-width: {logoHeight * 2.5}px; object-fit: contain;" />
        </div>
      {/if}

      {#if config.title}
        <h6 class="fw-bold mb-2">{config.title}</h6>
      {/if}

      {#if !ended}
        <CountdownDisplay
          {days} {hours} {minutes} {seconds}
          style={config.countdownStyle || 'SIMPLE'}
          size="small"
          isDark={isDarkTheme}
          daysLabel={$_('timer.days-short')}
          hoursLabel={$_('timer.hours-short')}
          minsLabel={$_('timer.mins-short')}
          secsLabel={$_('timer.secs-short')} />

        {#if config.message}
          <small style="opacity: 0.75;">{config.message}</small>
        {/if}
      {:else}
        <small>
          <i class="fa fa-check-circle me-1"></i>
          {$_('timer.ended')}
        </small>
      {/if}

      {#if config.showServerIp && siteInfo?.ipAddress}
        <div class="mt-2" style="opacity: 0.7;">
          <small style="font-size: 0.7rem; letter-spacing: 0.5px;">{$_('timer.server-ip')}</small>
          <div class="fw-bold" style="font-size: 0.85rem;">{siteInfo.ipAddress}</div>
        </div>
      {/if}
    </div>
  </div>
{/if}

<script>
    import {page} from '@panomc/sdk/svelte';
    import {_} from '../../../main';
    import CountdownDisplay from './CountdownDisplay.svelte';

    const browser = typeof window !== 'undefined';

  let config = $derived($page.data.countdownTimerConfig);
  let siteInfo = $derived($page.data.session?.siteInfo);

  function calcTimer(cfg) {
    if (!cfg?.enabled || !cfg.endDate) return { days: 0, hours: 0, minutes: 0, seconds: 0, ended: false };
    const diff = cfg.endDate - Date.now();
    if (diff <= 0) return { days: 0, hours: 0, minutes: 0, seconds: 0, ended: true };
    return {
      days: Math.floor(diff / (1000 * 60 * 60 * 24)),
      hours: Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)),
      minutes: Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60)),
      seconds: Math.floor((diff % (1000 * 60)) / 1000),
      ended: false,
    };
  }

  const initial = calcTimer($page.data.countdownTimerConfig);
  let days = $state(initial.days);
  let hours = $state(initial.hours);
  let minutes = $state(initial.minutes);
  let seconds = $state(initial.seconds);
  let ended = $state(initial.ended);
  let notified = $state(false);

  // Sidebar uses proportionally smaller sizes
  const logoSizes = { SMALL: 32, MEDIUM: 48, LARGE: 64, EXTRA_LARGE: 80, HUGE: 96, MASSIVE: 120 };
  let logoHeight = $derived(logoSizes[config?.logoSize] || 48);

  let isDarkTheme = $derived.by(() => {
    const theme = config?.theme || 'DARK';
    return theme === 'DARK' || theme === 'BLUR' || theme === 'GRADIENT';
  });

  let containerStyle = $derived.by(() => {
    if (!config) return '';
    const theme = config.theme || 'DARK';
    const bg = config.backgroundColor || '#000000';
    const hasBgImage = !!config.backgroundImage;

    if (hasBgImage) return 'background: transparent;';

    switch (theme) {
      case 'DARK':
        return `background: ${bg};`;
      case 'LIGHT':
        return `background: ${bg || '#f8f9fa'};`;
      case 'BLUR':
        return `background: rgba(0,0,0,0.6); backdrop-filter: blur(10px);`;
      case 'MINIMAL':
        return `background: ${bg || '#ffffff'};`;
      case 'GRADIENT':
        return `background: linear-gradient(135deg, ${config.gradientStart || '#667eea'}, ${config.gradientEnd || '#764ba2'});`;
      default:
        return `background: rgba(0,0,0,0.85);`;
    }
  });

  let overlayStyle = $derived.by(() => {
    if (!config) return '';
    const theme = config.theme || 'DARK';

    switch (theme) {
      case 'DARK':
        return 'background: rgba(0,0,0,0.75);';
      case 'LIGHT':
        return 'background: rgba(255,255,255,0.85);';
      case 'BLUR':
        return 'background: rgba(0,0,0,0.4); backdrop-filter: blur(10px); -webkit-backdrop-filter: blur(10px);';
      case 'MINIMAL':
        return 'background: transparent;';
      case 'GRADIENT':
        return `background: linear-gradient(135deg, ${config.gradientStart || '#667eea'}cc, ${config.gradientEnd || '#764ba2'}cc);`;
      default:
        return 'background: rgba(0,0,0,0.75);';
    }
  });

  function updateTimer() {
    const t = calcTimer(config);
    days = t.days;
    hours = t.hours;
    minutes = t.minutes;
    seconds = t.seconds;
    ended = t.ended;

    if (t.ended && !notified && browser) {
      notified = true;
    }
  }



  $effect(() => {
    updateTimer();
    const id = setInterval(updateTimer, 1000);

    return () => clearInterval(id);
  });
</script>
